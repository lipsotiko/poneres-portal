package com.poneres.portal.onboarding;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Onboarding {
    @Id
    private String id;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dob;
    private String gender;
    private String specialty;
    private String npi;
    private String citizenship_status;
    private String location;
    private String uncomfortableProcedures;
    private String haveYouDoneLocumsBefore;
    private String activeCertifications;
    private String malpractice;
    private String resumeId;
    private List<OnboardingLicenseFile> licenseFiles;
    private String employmentType;
    private List<String> schedulePreferences;
    private String createdBy;
    private LocalDateTime createdAt;
}
