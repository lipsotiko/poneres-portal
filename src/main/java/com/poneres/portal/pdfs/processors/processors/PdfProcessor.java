package com.poneres.portal.pdfs.processors.processors;

import java.util.Map;

public interface PdfProcessor {
    byte[] process(Map<String, Object> metadata, String patientSignatureId, String prescriberSignatureId);

    byte[] previewWithFieldsPopulated(Boolean skipAddedFields);
}
