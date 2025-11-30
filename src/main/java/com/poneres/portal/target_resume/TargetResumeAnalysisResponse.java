package com.poneres.portal.target_resume;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TargetResumeAnalysisResponse {
    private Integer score;
    private String parsedResume;
}
