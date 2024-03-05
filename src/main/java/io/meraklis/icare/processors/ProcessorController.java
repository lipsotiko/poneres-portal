package io.meraklis.icare.processors;

import io.meraklis.icare.applications.PatientApplicationType;
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
import java.io.IOException;
import java.util.Map;

import static io.meraklis.icare.processors.DocumentHelper.processField;
import static io.meraklis.icare.processors.DocumentHelper.setField;

@RestController
@RequestMapping("/api/processor")
public class ProcessorController {

    @Autowired
    private TextToImageBuilder textToImageBuilder;

    @Autowired
    private SignatureApplicator signatureApplicator;

    @Autowired
    private LillyCaresApplicationProcessorV1 lillyCaresApplicationProcessorV1;

    @Autowired
    private BoehringerCaresApplicationProcessorV1 boehringerCaresApplicationProcessorV1;

    @PostMapping(value = "/field-names", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] getDocumentWithFieldNames(
            @RequestParam PatientApplicationType type,
            @RequestParam Boolean setTextFields) throws IOException {
        try (PDDocument doc = Loader.loadPDF(new ClassPathResource(type.getFormPath()).getFile())) {
            for (PDField field : doc.getDocumentCatalog().getAcroForm().getFields()) {
                System.out.println(field.getPartialName());
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

    @PostMapping(value = "/preview", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] preview(@RequestParam PatientApplicationType type,
                                        @RequestBody Map<String, Object> data) throws IOException {
        if (type.equals(PatientApplicationType.LILLY_CARES_V1)) {
            return lillyCaresApplicationProcessorV1.process(data, null, null);
        } else if (type.equals(PatientApplicationType.BOEHRINGER_CARES_V1)) {
            return boehringerCaresApplicationProcessorV1.process(data, null, null);
        } else {
            return null;
        }
    }

//    @GetMapping(value = "/preview-with-text-signature", produces = MediaType.APPLICATION_PDF_VALUE)
//    public @ResponseBody byte[] previewWithTextSignature(@RequestParam PatientApplicationType type) throws IOException {
//        try (PDDocument doc = Loader.loadPDF(new ClassPathResource(type.getFormPath()).getFile())) {
//            List<SignatureConfig> configs = new ArrayList<>();
//
//            File johnWick = tmpFile(textToImageBuilder.convertToPng("John Wick"), ".png");
//            File saulGoodman = tmpFile(textToImageBuilder.convertToPng("Saul Goodman"), ".png");
//
//            base64ToBytes()
//
//            configs.add(SignatureConfig.builder().page(6).signature(johnWick).xPos(22).yPos(82).build());
//            configs.add(SignatureConfig.builder().page(7).signature(johnWick).xPos(22).yPos(82).build());
//            configs.add(SignatureConfig.builder().page(8).signature(saulGoodman).xPos(140).yPos(202).build());
//            configs.add(SignatureConfig.builder().page(9).signature(saulGoodman).xPos(22).yPos(223).build());
//            signatureApplicator.apply(doc, configs);
//
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            doc.save(byteArrayOutputStream);
//            doc.close();
//            return byteArrayOutputStream.toByteArray();
//        }
//    }
}
