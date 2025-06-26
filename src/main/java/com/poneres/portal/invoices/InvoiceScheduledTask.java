package com.poneres.portal.invoices;

import com.poneres.portal.email.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Component
public class InvoiceScheduledTask {

    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 0 * * * *")
    public void sendInvoice() {
        log.info("Sending test email");
        emailService.send("evangelos.poneres@gmail.com",  "test hourly email", "test.html");
    }
}
