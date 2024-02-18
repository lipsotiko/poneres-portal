package io.meraklis.icare.processors;

import java.io.File;
import java.util.Map;

public interface ApplicationProcessor {
    byte[] process(Map<String, Object> metadata, File patientSignature, File prescriberSignature);
}
