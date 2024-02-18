package io.meraklis.icare.applications;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientApplication {
    @Id
    private String id;
    private PatientApplicationType type;
    private Map<String, Object> metadata;
    private String patientSignature;
    private String prescriberSignature;

    public boolean isSignedByPatient() {
        return patientSignature != null;
    }

    public boolean isSignedByPrescriber() {
        return prescriberSignature != null;
    }

    public boolean isSigned() {
        return isSignedByPatient() || isSignedByPrescriber();
    }

    @SuppressWarnings("unused")
    public String getDisplayApplicationName() {
        return type.getDisplayName();
    }
}
