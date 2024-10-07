package io.meraklis.icare.agreements;

import io.meraklis.icare.pdfs.PdfType;
import io.meraklis.icare.processors.ProcessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/agreements")
public class AgreementController {

    @Autowired
    private ProcessorFactory processorFactory;

    @PostMapping(value = "/preview", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] preview(@RequestBody Map<String, Object> metadata) {
        return processorFactory.get(PdfType.LEASE_AGREEMENT_MD_V1).process(metadata, null, null);
    }
}
