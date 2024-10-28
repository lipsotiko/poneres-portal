package com.poneres.portal.pdfs.processors.processors;

import com.poneres.portal.agreements.SignatureRecipient;

import java.util.List;
import java.util.Map;

public interface PdfProcessor {
    byte[] process(Map<String, Object> metadata, List<SignatureRecipient> recipients);

    List<Map<String, Object>> signatureFields(List<SignatureRecipient> recipients);

}
