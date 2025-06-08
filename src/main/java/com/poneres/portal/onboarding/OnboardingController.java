package com.poneres.portal.onboarding;

import com.poneres.portal.security.auth0.AuthenticationService;
import com.poneres.portal.security.auth0.UserAuthorized;
import com.poneres.portal.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

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
    @UserAuthorized(value = { "isAdmin", "isTenant", "isProvider"})
    public void save(@RequestBody OnboardingRequest request) {
        String createdBy = authenticationService.getUserProfile().getId();

        String resumeId = randomUUID().toString();
        storageService.save(resumeId, new byte[0], request.getResumeFileName(), createdBy);

        Onboarding onboarding = request.getOnboarding();
        onboarding.setResumeId(resumeId);
        onboarding.setCreatedAt(LocalDateTime.now());
        onboarding.setCreatedBy(createdBy);
        onboardingRepository.save(onboarding);
    }

    @GetMapping
    @UserAuthorized(value = { "isAdmin", "isTenant"})
    public Page<Onboarding> get(Pageable pageable) {
        return onboardingRepository.findAll(pageable);
    }

}
