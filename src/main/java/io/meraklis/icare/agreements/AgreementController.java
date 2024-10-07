package io.meraklis.icare.agreements;

import io.meraklis.icare.pdfs.PdfType;
import io.meraklis.icare.pdfs.processors.ProcessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/agreements")
public class AgreementController {

    @Autowired
    private ProcessorFactory processorFactory;

    @Autowired
    private AgreementRepository agreementRepository;

    @GetMapping
    public Page<Agreement> get(Pageable pageable) {
        return agreementRepository.findAll(pageable);
    }

    @PostMapping(value = "/preview", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] preview(
            @RequestParam PdfType type,
            @RequestBody Map<String, Object> metadata) {
        return processorFactory.get(type).process(metadata, null, null);
    }
}
