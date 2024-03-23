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
    private ProcessorFactory processorFactory;

    @PostMapping(value = "/field-names", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] documentWithFieldNames(
            @RequestParam PatientApplicationType type,
            @RequestParam Boolean skipAddedFields) throws IOException {
        try (PDDocument doc = Loader.loadPDF(new ClassPathResource(type.getFormPath()).getFile())) {
            Map<String, String> stringStringMap = processorFactory.get(type).pdfFieldsMap();

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
        ApplicationProcessor applicationProcessor = processorFactory.get(type);
        return applicationProcessor.process(data, null, null);
    }
}
