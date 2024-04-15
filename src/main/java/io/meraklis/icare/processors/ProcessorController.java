package io.meraklis.icare.processors;

import io.meraklis.icare.applications.PatientApplication;
import io.meraklis.icare.applications.PatientApplicationType;
import io.meraklis.icare.images.TextToImageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

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
    public @ResponseBody byte[] previewWithFieldsPopulated(
            @RequestParam PatientApplicationType type,
            @RequestParam Boolean skipAddedFields) {
        return processorFactory.get(type).previewWithFieldsPopulated(skipAddedFields);
    }

    @PostMapping(value = "/preview", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] preview(@RequestParam PatientApplicationType type,
                                        @RequestBody Map<String, Object> data) throws IOException {
        PatientApplication application = new PatientApplication();
        application.setMetadata(data);
        return processorFactory.get(type).process(application);
    }
}
