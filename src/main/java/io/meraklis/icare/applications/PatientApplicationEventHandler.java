package io.meraklis.icare.applications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RepositoryEventHandler
public class PatientApplicationEventHandler {

    @Autowired
    private PatientApplicationRepository patientApplicationRepository;

    @HandleBeforeCreate
    public PatientApplication handlePatientApplicationBeforeSave(PatientApplication patientApplication) {
        if (patientApplication.getId() == null) return patientApplication;
        Optional<PatientApplication> optionalPatientApplication = patientApplicationRepository.findById(patientApplication.getId());
        optionalPatientApplication.ifPresent(application -> {
            if (application.isSigned()) {
                throw new RuntimeException("Application has already been signed, no further updates are allowed.");
            }
        });
        return null;
    }
}
