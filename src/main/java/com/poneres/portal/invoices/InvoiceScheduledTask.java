package com.poneres.portal.invoices;

import com.poneres.portal.email.EmailService;
import com.poneres.portal.pdfs.processors.PdfType;
import com.poneres.portal.pdfs.processors.processors.PdfProcessor;
import com.poneres.portal.pdfs.processors.processors.ProcessorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Component
public class InvoiceScheduledTask {

    @Autowired
    private ProcessorFactory processorFactory;

    @Autowired
    private EmailService emailService;

//    @Scheduled(cron = "0 0 * * * *")
    public void sendInvoice() {
        log.info("Sending test email");

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        String month = now.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

        PdfProcessor pdfProcessor = processorFactory.get(PdfType.KW_COMMISSION_INVOICE);

        Map<String, Object> invoiceFields = new HashMap<>();
        invoiceFields.put("invoice_id", "123");
        invoiceFields.put("invoice_due", now.plusDays(7).toString());
        invoiceFields.put("invoice_date", now.toString());
        invoiceFields.put("client", "Sara Garlisch");
        invoiceFields.put("address", "1761 W. Morse Ave #3N, Chicago, IL 60626");
        invoiceFields.put("services", "Property Management");
        invoiceFields.put("commission", "$188.65");

        byte[] invoice = pdfProcessor.process(PdfType.KW_COMMISSION_INVOICE, invoiceFields, Collections.emptyList(), false);
        emailService.send("evangelos.poneres@gmail.com", String.format("Invoice for Property Management Services - %s %s", month, year), "kw-commission-invoice.html", "invoice.pdf", invoice);
    }
}
