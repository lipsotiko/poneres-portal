package io.meraklis.icare.processors;

import java.util.Map;

public interface ApplicationProcessor {
    byte[] process(Map<String, Object> metadata, String patientSignature, String prescriberSignature);

    Map<String, String> pdfFieldsMap();
}
