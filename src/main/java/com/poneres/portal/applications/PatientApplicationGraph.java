package com.poneres.portal.applications;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientApplicationGraph {
    public PatientApplication application;
    public String patientSignature;
    public String prescriberSignature;
    public Boolean allDocumentsUploaded;
}
