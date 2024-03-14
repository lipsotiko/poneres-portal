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

    @Autowired
    private NovoNordiskApplicationProcessorV1 novoNordiskApplicationProcessorV1;

    @PostMapping(value = "/field-names", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] documentWithFieldNames(@RequestParam PatientApplicationType type, @RequestParam Boolean skipAddedFields) throws IOException {
        try (PDDocument doc = Loader.loadPDF(new ClassPathResource(type.getFormPath()).getFile())) {
            Map<String, String> stringStringMap;
            if (type.equals(PatientApplicationType.LILLY_CARES_V1)) {
                stringStringMap = lillyCaresApplicationProcessorV1.pdfFieldsMap();
            } else if (type.equals(PatientApplicationType.BOEHRINGER_CARES_V1)) {
                stringStringMap = boehringerCaresApplicationProcessorV1.pdfFieldsMap();
            } else if (type.equals(PatientApplicationType.NOVO_NORDISK_V1)) {
                stringStringMap = novoNordiskApplicationProcessorV1.pdfFieldsMap();
            } else {
                return null;
            }

            for (PDField field : doc.getDocumentCatalog().getAcroForm().getFields()) {
                if (skipAddedFields && stringStringMap.containsKey(field.getPartialName())) {
                    continue;
                }
                System.out.println(field.getPartialName());
                if (field instanceof PDTextField) {
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
        } else if (type.equals(PatientApplicationType.NOVO_NORDISK_V1)) {
            return novoNordiskApplicationProcessorV1.process(data, null, null);
        } else {
            return null;
        }
    }
}
