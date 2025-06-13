package com.poneres.portal;

import com.poneres.portal.agreements.AgreementRepository;
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
    private StorageService storageService;

    @PostConstruct
    public void init() {
        super.init();
        agreementRepository.findAll().forEach(agreement -> {
            agreementRepository.delete(agreement);
        });

        onboardingRepository.findAll().forEach(onboarding -> {
            storageService.delete(onboarding.getResumeId());
            storageService.delete(onboarding.getLicenseId());
            onboardingRepository.delete(onboarding);
        });
    }
}
