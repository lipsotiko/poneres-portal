package com.poneres.portal.invoices;

import com.poneres.portal.email.EmailService;
import com.poneres.portal.pdfs.processors.PdfType;
import com.poneres.portal.pdfs.processors.processors.PdfProcessor;
import com.poneres.portal.pdfs.processors.processors.ProcessorFactory;
import com.poneres.portal.storage.StorageResponse;
import com.poneres.portal.storage.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;

@Slf4j
@Component
public class InvoiceScheduledTask {

    @Autowired
    private InvoiceJobRepository invoiceJobRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ProcessorFactory processorFactory;

    @Autowired
    private StorageService storageService;

    @Autowired
    private EmailService emailService;

//    @Scheduled(cron = "0 0 * * * *")
//    @Scheduled(fixedRate = 60000) // every 15 seconds
    public void sendInvoice() {
        invoiceJobRepository.findAll().stream()
                .filter(InvoiceJob::isScheduleEnabled)
                .forEach(this::sendInvoice);
    }

    public void triggerResendExistingInvoice(String invoiceId) {
        if (invoiceId != null) {
            log.info("Resending existing invoice");
            Optional<Invoice> existingInvoice = invoiceRepository.findById(invoiceId);
            existingInvoice.ifPresent(invoice -> {

                String to = invoice.getTo();
                String cc = invoice.getCc();
                String services = invoice.getServices();
                StorageResponse storageResponse = storageService.get(invoiceId);
                byte[] invoicePdf = storageResponse.getContent();
                sendEmail(to, cc, services, invoice.getCreatedAt(), invoicePdf);
            });
        }
    }

    public String triggerOneTimeNewInvoice(String invoiceJobId) {
        return invoiceJobRepository.findById(invoiceJobId).map(this::sendInvoice).orElseThrow();
    }

    private String sendInvoice(InvoiceJob invoiceJob) {
        log.info("Creating new invoice");
        LocalDateTime now = LocalDateTime.now();

        Map<String, String> jobMetadata = invoiceJob.getJobMetadata();
        String to = jobMetadata.get("to");
        String cc = jobMetadata.get("cc");
        String services = jobMetadata.get("services");

        Invoice savedInvoice = invoiceRepository.save(Invoice.builder()
                .type(PdfType.KW_COMMISSION_INVOICE)
                .to(to)
                .cc(cc)
                .services(services)
                .createdAt(now)
                .build());

        Map<String, Object> pdfMetadata = invoiceJob.getPdfMetadata();
        pdfMetadata.put("invoice_id", savedInvoice.getId().substring(0, 7));
        pdfMetadata.put("invoice_due", dueDate(now));
        pdfMetadata.put("invoice_date", now.toLocalDate().toString());

        PdfProcessor pdfProcessor = processorFactory.get(PdfType.KW_COMMISSION_INVOICE);
        byte[] invoicePdf = pdfProcessor.process(PdfType.KW_COMMISSION_INVOICE, pdfMetadata, Collections.emptyList(), false);
        storageService.save(savedInvoice.getId(), invoicePdf,  "invoice.pdf", "system");

        sendEmail(to, cc, services, now, invoicePdf);

        return savedInvoice.getId();
    }

    private void sendEmail(String to, String cc, String services, LocalDateTime invoiceDate, byte[] invoicePdf) {
        String month = invoiceDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        Integer year = invoiceDate.getYear();

        String subject = String.format("Invoice for %s Services - %s %s", services, month, year);

        Map<String, String> tokens = new HashMap<>(){{
            put("SERVICES", services);
            put("DUE_DATE", dueDate(invoiceDate));
        }};

        emailService.send(to, cc, subject, "kw-commission-invoice.html", "invoice.pdf", invoicePdf, tokens);
    }

    private String dueDate(LocalDateTime invoiceDate) {
        return invoiceDate.toLocalDate().plusDays(7).toString();
    }
}
