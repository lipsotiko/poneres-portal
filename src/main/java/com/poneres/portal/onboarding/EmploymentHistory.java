package com.poneres.portal.onboarding;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmploymentHistory {
    private String employerName;
    private String startDate;
    private String endDate;
    private String positionDetails;
    private String supervisorName;
    private String supervisorContact;
}
