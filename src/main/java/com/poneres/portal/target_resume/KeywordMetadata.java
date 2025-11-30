package com.poneres.portal.target_resume;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class KeywordMetadata {
    private String word;
    private Long count;
    private List<String> context;
}
