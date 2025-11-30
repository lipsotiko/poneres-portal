package com.poneres.portal.target_resume;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class TargetResumeAnalysisResponse {
    private Integer score;
    private String parsedResume;
    private Map<String, Long> jobDescriptionKeywordCounts;
}
