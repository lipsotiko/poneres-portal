package com.poneres.portal.onboarding;

import com.poneres.portal.security.auth0.AuthenticationService;
import com.poneres.portal.security.auth0.UserAuthorized;
import com.poneres.portal.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.poneres.portal.helpers.Helpers.base64ToBytes;
import static java.util.UUID.randomUUID;

@RestController
@RequestMapping("/api/onboarding")
public class OnboardingController {

    @Autowired
    private OnboardingRepository onboardingRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private StorageService storageService;

    @PostMapping
    @UserAuthorized(value = { "isAdmin", "isProvider"})
    public void save(@RequestBody OnboardingRequest request) {
        Onboarding onboarding = request.getOnboarding();

        String createdBy = authenticationService.getUserProfile().getId();
        onboarding.setCreatedBy(createdBy);

        String resumeId = randomUUID().toString();
        byte[] resume = base64ToBytes(request.getResumeDataURL());
        storageService.save(resumeId, resume, request.getResumeFileName(), createdBy);
        onboarding.setResumeFileId(resumeId);

        for(int i = 0; i < request.getLicenseFiles().size(); i++) {
            OnboardingLicenseFileRequest lf = request.getLicenseFiles().get(i);
            String licenseId = randomUUID().toString();
            byte[] license = base64ToBytes(lf.getDataURL());
            storageService.save(licenseId, license, lf.getFileName(), createdBy);
            onboarding.getLicenseFiles().get(i).setLicenseFileId(licenseId);
        }

        String governmentIdentificationId = randomUUID().toString();
        byte[] govId = base64ToBytes(request.getGovIdDataURL());
        storageService.save(governmentIdentificationId, govId, request.getGovIdFileName(), createdBy);
        onboarding.setGovIdFileId(governmentIdentificationId);

        onboarding.setCreatedAt(LocalDateTime.now());
        onboardingRepository.save(onboarding);
    }

    @GetMapping("/{id}")
    @UserAuthorized(value = { "isAdmin" })
    public Optional<Onboarding> get(@PathVariable("id") String id) {
        return onboardingRepository.findById(id);
    }

    @DeleteMapping("/{id}")
    @UserAuthorized(value = { "isAdmin" })
    public void delete(@PathVariable("id") String id) {
        Optional<Onboarding> onboarding = onboardingRepository.findById(id);
        onboarding.ifPresent(o -> {
            storageService.delete(o.getResumeFileId());
            storageService.delete(o.getGovIdFileId());
            o.getLicenseFiles().forEach(l -> storageService.delete(l.getLicenseFileId()));
            onboardingRepository.delete(o);
        });
    }

    @DeleteMapping("/multiple")
    @UserAuthorized(value = { "isAdmin" })
    public void delete(@RequestBody DeleteOnboardingRequest deleteOnboardingRequest) {
        deleteOnboardingRequest.getOnboardingIds().forEach(id -> {
            Optional<Onboarding> onboarding = onboardingRepository.findById(id);
            onboarding.ifPresent(o -> {
                storageService.delete(o.getResumeFileId());
                onboardingRepository.delete(o);
            });
        });
    }

    @GetMapping
    @UserAuthorized(value = { "isAdmin" })
    public Page<Onboarding> get(Pageable pageable) {
        return onboardingRepository.findAll(pageable);
    }

}
