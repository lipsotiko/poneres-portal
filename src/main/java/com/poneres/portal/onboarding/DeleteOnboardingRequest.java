package com.poneres.portal.onboarding;

import lombok.Data;

import java.util.List;

@Data
public class DeleteOnboardingRequest {
    private List<String> onboardingIds;
}
