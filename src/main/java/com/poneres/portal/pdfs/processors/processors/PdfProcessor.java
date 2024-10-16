package com.poneres.portal.pdfs.processors.processors;

import java.util.Map;

public interface PdfProcessor {
    byte[] process(Map<String, Object> metadata, Boolean fieldsPreview, String patientSignatureId, String prescriberSignatureId);
}
