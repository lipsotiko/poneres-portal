package io.meraklis.icare.documents;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientDocumentRepository extends MongoRepository<PatientDocument, String> {
    List<PatientDocument> findByApplicationId(String applicationId);
}
