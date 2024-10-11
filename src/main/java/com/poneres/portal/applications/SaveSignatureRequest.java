package com.poneres.portal.applications;

import lombok.Data;
import lombok.Getter;

@Data
public class SaveSignatureRequest {
    @Getter
    private boolean clear;
    private String signature;
}
