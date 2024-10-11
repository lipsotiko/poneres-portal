package com.poneres.portal.documents;

import com.poneres.portal.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientDocumentService {
    @Autowired
    private StorageService storage;

    @Autowired
    private PatientDocumentRepository patientDocumentRepository;

    public void delete(String applicationId) {
        patientDocumentRepository.findByApplicationId(applicationId).forEach(d -> {
            storage.delete(d.getId());
            patientDocumentRepository.delete(d);
        });
    }
}
