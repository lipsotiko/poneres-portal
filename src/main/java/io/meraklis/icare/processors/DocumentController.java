package io.meraklis.icare.processors;

import io.meraklis.icare.images.TextToImageBuilder;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static io.meraklis.icare.helpers.Helpers.tmpFile;
import static io.meraklis.icare.processors.DocumentHelper.processField;
import static io.meraklis.icare.processors.DocumentHelper.setField;

@RestController
@RequestMapping("/api/document")
public class DocumentController {

    @Autowired
    private TextToImageBuilder textToImageBuilder;

    @PostMapping(value = "/field-names", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] getDocumentWithFieldNames(@RequestParam Boolean setTextFields) throws IOException {
        try (PDDocument doc = Loader.loadPDF(new ClassPathResource("pdfs/lilly_cares_v1.pdf").getFile())) {
            for (PDField field : doc.getDocumentCatalog().getAcroForm().getFields()) {
                if (field instanceof PDTextField && setTextFields) {
                    setField(doc, field.getPartialName(), field.getPartialName());
                } else if (field instanceof PDCheckBox) {
                    setField(doc, field.getPartialName(), "X");
                } else {
                    processField(field, "|--", field.getPartialName());
                }
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            doc.save(byteArrayOutputStream);
            doc.close();
            return byteArrayOutputStream.toByteArray();
        }
    }

    @GetMapping(value = "/preview-with-text-signature", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] previewWithTextSignature(@RequestParam("signature") String signature) throws IOException, FontFormatException {
        try (PDDocument doc = Loader.loadPDF(new ClassPathResource("pdfs/lilly_cares_v1.pdf").getFile())) {

            File signatureFile = tmpFile(textToImageBuilder.convertToPng("John Wick"), ".png");
            PDImageXObject pdImage = PDImageXObject.createFromFileByExtension(signatureFile, doc);

            PDPageContentStream contents = new PDPageContentStream(doc, doc.getPage(1));
            contents.drawImage(pdImage, 250, 300);
            contents.close();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            doc.save(byteArrayOutputStream);
            doc.close();
            return byteArrayOutputStream.toByteArray();
        }
    }
}
