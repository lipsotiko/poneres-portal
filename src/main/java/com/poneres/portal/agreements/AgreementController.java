package com.poneres.portal.agreements;

import com.poneres.portal.pdfs.processors.PdfType;
import com.poneres.portal.pdfs.processors.processors.ProcessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @PostMapping
    public Agreement save(@RequestBody Agreement agreement) {
        agreement.setCreatedAt(LocalDateTime.now());
        return agreementRepository.save(agreement);
    }
}
