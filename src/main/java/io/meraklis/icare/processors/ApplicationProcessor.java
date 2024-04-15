package io.meraklis.icare.processors;

import io.meraklis.icare.applications.PatientApplication;

public interface ApplicationProcessor {
    byte[] process(PatientApplication application);

    byte[] previewWithFieldsPopulated(Boolean skipAddedFields);
}
