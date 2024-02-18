package io.meraklis.icare.processors;

import io.meraklis.icare.images.TextToImageBuilder;
import lombok.SneakyThrows;
import org.apache.pdfbox.multipdf.Overlay;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SignatureApplicator {

    @Autowired
    private TextToImageBuilder textToImageBuilder;

    public void apply(PDDocument doc, List<SignatureConfig> configs) {
        try (Overlay overlay = new Overlay()) {
            overlay.setInputPDF(doc);

            Map<Integer, PDDocument> overlays = new HashMap<>();

            for (SignatureConfig config : configs) {
                PDDocument signatureDoc =
                        buildSignatureDocument(config.getSignature(), config.getXPos(), config.getYPos());
                overlays.put(config.getPage(), signatureDoc);
            }

            overlay.overlayDocuments(overlays);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    private PDDocument buildSignatureDocument(File signatureFile, int xPos, int yPos) {
        PDPage signaturePage = new PDPage();
        PDDocument signatureDoc = new PDDocument();
        signatureDoc.addPage(signaturePage);

        PDPageContentStream contents = new PDPageContentStream(signatureDoc, signaturePage);
        PDImageXObject pdImage = PDImageXObject.createFromFileByExtension(signatureFile, new PDDocument());
        contents.drawImage(pdImage, xPos, yPos);
        contents.close();

        return signatureDoc;
    }
}
