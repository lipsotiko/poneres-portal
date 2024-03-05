package io.meraklis.icare.processors;

import io.meraklis.icare.images.TextToImageBuilder;
import lombok.SneakyThrows;
import org.apache.pdfbox.multipdf.Overlay;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.meraklis.icare.helpers.Helpers.base64ToFile;
import static io.meraklis.icare.helpers.Helpers.tmpFile;

@Service
public class SignatureApplicator {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Autowired
    private TextToImageBuilder textToImageBuilder;

    public void apply(PDDocument doc, List<SignatureConfig> configs) {
        try (Overlay overlay = new Overlay()) {
            overlay.setInputPDF(doc);

            Map<Integer, PDDocument> overlays = new HashMap<>();

            for (SignatureConfig config : configs) {
                File signatureFile = base64ToFile(config.getSignatureBase64());
                BufferedImage image = ImageIO.read(signatureFile);

                if (activeProfile.equals("develop")) {
                    applyBorder(image);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    try {
                        ImageIO.write(image, "png", baos);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    signatureFile = tmpFile(baos.toByteArray(), ".png");
                }

                int width = image.getWidth();
                int height = image.getHeight();
                int maxHeight = config.getMaxHeight();

                if (height > maxHeight && maxHeight != 0) {
                    double scalePercent = maxHeight / (double) height;
                    width = (int) Math.round(width * scalePercent);
                    height = (int) Math.round(height * scalePercent);
                }

                PDDocument signatureDoc =
                        buildSignatureDocument(signatureFile, config.getXPos(), config.getYPos(), width, height);
                overlays.put(config.getPage(), signatureDoc);
            }
            overlay.setOverlayPosition(Overlay.Position.FOREGROUND);
            overlay.overlayDocuments(overlays);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    private PDDocument buildSignatureDocument(File signatureFile, int xPos, int yPos, int w, int h) {
        PDPage signaturePage = new PDPage();
        PDDocument signatureDoc = new PDDocument();
        signatureDoc.addPage(signaturePage);

        PDPageContentStream contents = new PDPageContentStream(signatureDoc, signaturePage);
        PDImageXObject pdImage = PDImageXObject.createFromFileByExtension(signatureFile, new PDDocument());
        contents.drawImage(pdImage, xPos, yPos, w, h);
        contents.close();

        return signatureDoc;
    }


    private static void applyBorder(BufferedImage image) {
        int borderColor = 0x99FF0000;
        for (int i = 0; i < image.getWidth(); i++) {
            image.setRGB(i, image.getHeight() - 1, borderColor);
        }

        for (int i = 0; i < image.getWidth(); i++) {
            image.setRGB(i, 1, borderColor);
        }

        for (int i = 0; i < image.getHeight(); i++) {
            image.setRGB(1, i, borderColor);
        }

        for (int i = 0; i < image.getHeight(); i++) {
            image.setRGB(image.getWidth() - 1, i, borderColor);
        }

        int middleLine = image.getHeight() / 2;
        for (int i = 0; i < image.getWidth(); i++) {
            image.setRGB(i, middleLine, borderColor);
        }
    }
}
