package com.poneres.portal.onboarding;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EducationDetails {
    private String schoolName;
    private String location;
    private String degree;
    private LocalDate graduationDate;
}
