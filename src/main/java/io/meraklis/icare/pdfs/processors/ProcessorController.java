package io.meraklis.icare.pdfs.processors;

import io.meraklis.icare.helpers.TextToImageBuilder;
import io.meraklis.icare.pdfs.PdfType;
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
            @RequestParam PdfType type,
            @RequestParam Boolean skipAddedFields) {
        return processorFactory.get(type).previewWithFieldsPopulated(skipAddedFields);
    }

    @PostMapping(value = "/preview", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] preview(@RequestParam PdfType type,
                                        @RequestBody Map<String, Object> data) throws IOException {
        return processorFactory.get(type).process(data, null, null);
    }
}
