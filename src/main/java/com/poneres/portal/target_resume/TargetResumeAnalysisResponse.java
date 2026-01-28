package com.poneres.portal.target_resume;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TargetResumeAnalysisResponse {
    private double score;
    private String parsedResume;
    private List<KeywordMetadata> jobDescriptionKeywords;
}
