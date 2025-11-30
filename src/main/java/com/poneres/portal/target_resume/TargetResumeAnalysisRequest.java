package com.poneres.portal.target_resume;

import lombok.Data;

@Data
public class TargetResumeAnalysisRequest {
    private TargetResumeMetadata metadata;
    private String resumeDataURL;
    private String resumeFileName;
}
