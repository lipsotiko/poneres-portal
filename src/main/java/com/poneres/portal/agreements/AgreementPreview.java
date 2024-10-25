package com.poneres.portal.agreements;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AgreementPreview {
    private Map<String, Object> metadata;
    private List<SignatureRecipient> recipients;
}
