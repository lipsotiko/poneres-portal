package com.poneres.portal.target_resume;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KeywordMetadata {
    private String word;
    private Long count;
}
