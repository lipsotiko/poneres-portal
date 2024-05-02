package io.meraklis.icare.applications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientApplicationService {

    @Autowired
    private PatientApplicationRepository applicationRepository;

    public void save(PatientApplication application) {
        application.setComplete(isComplete((application)));
        applicationRepository.save(application);
    }

    private boolean isComplete(PatientApplication patientApplication) {
        return patientApplication.isSignedByPatient()
                && patientApplication.isSignedByPrescriber()
                && !patientApplication.getSubmitted();
    }
}
