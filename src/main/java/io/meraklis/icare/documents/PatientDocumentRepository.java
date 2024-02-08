package io.meraklis.icare.documents;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "patient-documents")
public interface PatientDocumentRepository extends MongoRepository<PatientDocument, String> {
    List<PatientDocument> findByApplicationId(String applicationId);
}
