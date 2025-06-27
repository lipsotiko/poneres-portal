package com.poneres.portal.invoices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceScheduledTask invoiceScheduledTask;

    @PostMapping("/trigger")
    public void trigger() {
        invoiceScheduledTask.sendInvoice();
    }

}
