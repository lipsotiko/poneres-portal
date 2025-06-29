package com.poneres.portal;

import com.poneres.portal.agreements.AgreementRepository;
import com.poneres.portal.invoices.InvoiceRepository;
import com.poneres.portal.onboarding.OnboardingRepository;
import com.poneres.portal.storage.StorageService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"local", "local-no-auth"})
public class PoneresPortalInitLocal extends AbstractPoneresPortalInit {

    @Autowired
    private AgreementRepository agreementRepository;

    @Autowired
    private OnboardingRepository onboardingRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private StorageService storageService;

    @PostConstruct
    public void init() {
        super.init();
        agreementRepository.findAll().forEach(agreement -> {
            agreementRepository.delete(agreement);
        });

        onboardingRepository.findAll().forEach(onboarding -> {
            storageService.delete(onboarding.getResumeId());
            onboarding.getLicenseFiles().forEach(l -> storageService.delete(l.getLicenseId()));
            onboardingRepository.delete(onboarding);
        });

        invoiceRepository.findAll().forEach(invoice -> {
            storageService.delete(invoice.getId());
            invoiceRepository.delete(invoice);
        });
    }
}
