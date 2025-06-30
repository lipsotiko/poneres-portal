package com.poneres.portal.invoices;

import com.poneres.portal.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceScheduledTask invoiceScheduledTask;

    @Autowired
    private StorageService storageService;

    @GetMapping("/")
    public List<Invoice> invoiceList() {
        return invoiceScheduledTask.invoiceList();
    }

    @GetMapping("/jobs")
    public List<InvoiceJob> invoiceJobList() {
        return invoiceScheduledTask.invoiceJobsList();
    }

    @PostMapping("/trigger/resend-existing/{invoiceId}")
    public void resendExistingInvoice(@PathVariable("invoiceId") String invoiceId) {
        invoiceScheduledTask.triggerResendExistingInvoice(invoiceId);
    }

    @PostMapping("/trigger/one-time-new/{invoiceJobId}")
    public String triggerOneTimeNewInvoice(@PathVariable("invoiceJobId") String invoiceJobId) {
        return invoiceScheduledTask.triggerOneTimeNewInvoice(invoiceJobId);
    }

}
