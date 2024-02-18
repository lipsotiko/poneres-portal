package io.meraklis.icare.processors;

import io.meraklis.icare.images.TextToImageBuilder;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.meraklis.icare.helpers.Helpers.tmpFile;
import static io.meraklis.icare.processors.DocumentHelper.processField;
import static io.meraklis.icare.processors.DocumentHelper.setField;

@RestController
@RequestMapping("/api/processor")
public class ProcessorController {

    @Autowired
    private TextToImageBuilder textToImageBuilder;

    @Autowired
    private SignatureApplicator signatureApplicator;

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
    public @ResponseBody byte[] previewWithTextSignature() throws IOException {
        try (PDDocument doc = Loader.loadPDF(new ClassPathResource("pdfs/lilly_cares_v1.pdf").getFile())) {
            List<SignatureConfig> configs = new ArrayList<>();

            File johnWick = tmpFile(textToImageBuilder.convertToPng("John Wick"), ".png");
            File saulGoodman = tmpFile(textToImageBuilder.convertToPng("Saul Goodman"), ".png");

            configs.add(SignatureConfig.builder().page(6).signature(johnWick).xPos(22).yPos(82).build());
            configs.add(SignatureConfig.builder().page(7).signature(johnWick).xPos(22).yPos(82).build());
            configs.add(SignatureConfig.builder().page(8).signature(saulGoodman).xPos(140).yPos(202).build());
            configs.add(SignatureConfig.builder().page(9).signature(saulGoodman).xPos(22).yPos(223).build());
            signatureApplicator.apply(doc, configs);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            doc.save(byteArrayOutputStream);
            doc.close();
            return byteArrayOutputStream.toByteArray();
        }
    }
}
