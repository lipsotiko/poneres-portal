package com.poneres.portal.agreements;

import com.poneres.portal.pdfs.processors.PdfType;
import com.poneres.portal.pdfs.processors.processors.PdfProcessor;
import com.poneres.portal.pdfs.processors.processors.ProcessorFactory;
import com.poneres.portal.signatures.SignatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.poneres.portal.helpers.Helpers.bytesToBase64;

@RestController
@RequestMapping("/api/agreements")
public class AgreementController {

    @Autowired
    private ProcessorFactory processorFactory;

    @Autowired
    private AgreementRepository agreementRepository;

    @Autowired
    private SignatureService signatureService;

    @GetMapping
    public Page<Agreement> get(Pageable pageable) {
        return agreementRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Optional<Agreement> get(@PathVariable("id") String agreementId) {
        return agreementRepository.findById(agreementId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String agreementId) {
        agreementRepository.findById(agreementId).ifPresent(agreement -> {
            signatureService.delete(agreement.getSsdId());
            agreementRepository.deleteById(agreement.getId());
        });
    }

    @PostMapping(value = "/preview", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] preview(@RequestParam PdfType type,
                          @RequestBody AgreementPreview agreementPreview) {
        Map<String, Object> metadata = agreementPreview.getMetadata();
        List<SignatureRecipient> recipients = agreementPreview.getRecipients();
        return processorFactory.get(type).process(type, metadata, recipients);
    }

    @PostMapping(value = "/{id}/copy", produces = MediaType.APPLICATION_PDF_VALUE)
    public void copy(@PathVariable("id") String agreementId) {
        agreementRepository.findById(agreementId).ifPresent(agreement -> {
            Agreement copy = Agreement.builder()
                    .type(agreement.getType())
                    .metadata(agreement.getMetadata())
                    .createdAt(LocalDateTime.now())
                    .build();
            agreementRepository.save(copy);
        });
    }

    @PostMapping
    public Agreement save(@RequestBody Agreement agreement) {
        if (agreement.getId() != null) {
            agreementRepository.findById(agreement.getId()).ifPresent(existingAgreement -> {
                if (existingAgreement.isSent()) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Sent documents may not be updated.");
                }
            });
        }
        agreement.setCreatedAt(LocalDateTime.now());
        return agreementRepository.save(agreement);
    }

    @PostMapping("/{id}/send")
    public void sendAgreementToBeSigned(@PathVariable("id") String agreementId) {
        agreementRepository.findById(agreementId).ifPresent(agreement -> {
            String ssid = sendForSigning(agreement);
            agreement.setSsdId(ssid);
            agreementRepository.save(agreement);
        });
    }

    @GetMapping("/{id}/status")
    public String status(@PathVariable("id") String agreementId) {
        return agreementRepository.findById(agreementId).map(agreement -> {
            String ssdId = agreement.getSsdId();
            if (ssdId == null) {
                return "None";
            }
            return signatureService.status(ssdId);
        }).orElse(null);
    }

    @GetMapping("/{id}/file-url")
    public String fileUrl(@PathVariable("id") String agreementId) {
        return agreementRepository.findById(agreementId).map(agreement -> {
            String ssdId = agreement.getSsdId();
            return signatureService.fileUrl(ssdId);
        }).orElse(null);
    }

    @PostMapping("/{id}/send-reminder")
    public void sendReminder(@PathVariable("id") String agreementId) {
        agreementRepository.findById(agreementId).ifPresent(agreement -> signatureService.sendReminder(agreement.getSsdId()));
    }

    private String sendForSigning(Agreement agreement) {
        PdfType type = agreement.getType();
        Map<String, Object> metadata = agreement.getMetadata();
        String fileName = agreement.getFileName();
        List<SignatureRecipient> recipients = agreement.getRecipients();

        PdfProcessor pdfProcessor = processorFactory.get(type);
        byte[] fileBytes = pdfProcessor.process(type, metadata, Collections.emptyList());
        List<Map<String, Object>> signatureFields = pdfProcessor.signatureFields(recipients);
        String fileBase64 = bytesToBase64(fileBytes);
        return signatureService.create(fileName, fileBase64, recipients, signatureFields);
    }
}
