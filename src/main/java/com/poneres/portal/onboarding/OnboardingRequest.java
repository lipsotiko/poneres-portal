package com.poneres.portal.onboarding;

import lombok.Data;

import java.util.List;

@Data
public class OnboardingRequest {
    private Onboarding onboarding;
    private String resumeDataURL;
    private String resumeFileName;
    private String govIdDataURL;
    private String govIdFileName;
    private List<OnboardingLicenseFileRequest> licenseFiles;
}
