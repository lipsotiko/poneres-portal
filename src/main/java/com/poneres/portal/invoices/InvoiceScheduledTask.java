package com.poneres.portal.invoices;

import com.poneres.portal.email.EmailService;
import com.poneres.portal.pdfs.processors.PdfType;
import com.poneres.portal.pdfs.processors.processors.PdfProcessor;
import com.poneres.portal.pdfs.processors.processors.ProcessorFactory;
import com.poneres.portal.storage.StorageResponse;
import com.poneres.portal.storage.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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

    @Scheduled(cron = "0 0 4 1 * *")
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
                StorageResponse storageResponse = storageService.get(invoiceId);
                byte[] invoicePdf = storageResponse.getContent();
                sendEmail(invoice, invoicePdf);
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
        String client = jobMetadata.get("client");
        String to = jobMetadata.get("to");
        String cc = jobMetadata.get("cc");
        String services = jobMetadata.get("services");

        Invoice savedInvoice = invoiceRepository.save(Invoice.builder()
                .type(PdfType.KW_COMMISSION_INVOICE)
                .client(client)
                .to(to)
                .cc(cc)
                .services(services)
                .createdAt(now)
                .build());

        Map<String, Object> pdfMetadata = invoiceJob.getPdfMetadata();
        pdfMetadata.put("invoice_id", fmtInvoiceId(savedInvoice));
        pdfMetadata.put("invoice_due", dueDate(now));
        pdfMetadata.put("invoice_date", now.toLocalDate().toString());

        PdfProcessor pdfProcessor = processorFactory.get(PdfType.KW_COMMISSION_INVOICE);
        byte[] invoicePdf = pdfProcessor.process(PdfType.KW_COMMISSION_INVOICE, pdfMetadata, Collections.emptyList(), false, false);
        storageService.save(savedInvoice.getId(), invoicePdf, "invoice.pdf", "system");

        sendEmail(savedInvoice, invoicePdf);

        return savedInvoice.getId();
    }

    private void sendEmail(Invoice invoice, byte[] invoicePdf) {
        String to = invoice.getTo();
        String cc = invoice.getCc();
        LocalDateTime invoiceDate = invoice.getCreatedAt();
        String month = invoiceDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        Integer year = invoiceDate.getYear();

        String subject = String.format("Your Invoice for %s Services is Ready - %s %s", invoice.getServices(), month, year);

        Map<String, String> tokens = new HashMap<>() {{
            put("CLIENT", invoice.getClient());
            put("SERVICES", invoice.getServices());
            put("DUE_DATE", dueDate(invoiceDate));
            put("INVOICE_NUMBER", fmtInvoiceId(invoice));
        }};

        emailService.send(to, cc, subject, "kw-commission-invoice.html", "invoice.pdf", invoicePdf, tokens);
    }

    private String dueDate(LocalDateTime invoiceDate) {
        return invoiceDate.toLocalDate().plusDays(7).toString();
    }

    private String fmtInvoiceId(Invoice invoice) {
        return invoice.getId().substring(0, 7);
    }

    public List<InvoiceJob> invoiceJobsList() {
        return invoiceJobRepository.findAll();
    }

    public List<Invoice> invoiceList() {
        return invoiceRepository.findAll();
    }
}
