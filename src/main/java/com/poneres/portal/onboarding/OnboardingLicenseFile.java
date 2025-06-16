package com.poneres.portal.onboarding;

import lombok.Data;

import java.time.LocalDate;

@Data
public class OnboardingLicenseFile {
    private String licenseId;
    private String state;
    private String licenseNumber;
    private LocalDate expirationDate;
}
