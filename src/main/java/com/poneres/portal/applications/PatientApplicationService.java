package com.poneres.portal.applications;

import com.poneres.portal.documents.DocumentType;
import com.poneres.portal.documents.PatientDocument;
import com.poneres.portal.documents.PatientDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.poneres.portal.documents.DocumentType.*;

@Service
public class PatientApplicationService {

    @Autowired
    private PatientApplicationRepository applicationRepository;

    @Autowired
    private PatientDocumentRepository documentRepository;

    public void save(PatientApplication application) {
        application.setComplete(isComplete((application)));
        application.setAllDocumentsUploaded(allDocumentsUploaded((application)));
        applicationRepository.save(application);
    }

    private Boolean allDocumentsUploaded(PatientApplication application) {
        List<PatientDocument> documents = documentRepository.findByApplicationId(application.getId());
        Set<DocumentType> documentTypes = documents.stream().map(PatientDocument::getType).collect(Collectors.toSet());

        return documentTypes.contains(MEDICARE_PART_A_B)
                && documentTypes.contains(MEDICARE_PART_D)
                && documentTypes.contains(STATE_ID)
                && documentTypes.contains(SSA_1099);
    }

    private boolean isComplete(PatientApplication application) {
        return allDocumentsUploaded(application)
                && application.isSignedByPatient()
                && application.isSignedByPrescriber()
                && !application.getSubmitted();
    }

    public void updateApplicationStatus(String applicationId) {
        Optional<PatientApplication> byId = applicationRepository.findById(applicationId);
        byId.ifPresent(this::save);
    }
}
