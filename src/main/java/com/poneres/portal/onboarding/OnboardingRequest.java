package com.poneres.portal.onboarding;

import lombok.Data;

@Data
public class OnboardingRequest {
    private Onboarding onboarding;
    private String resumeDataURL;
    private String resumeFileName;
}
