package io.meraklis.icare.applications;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientApplicationRepository extends MongoRepository<PatientApplication, String> {
    Page<PatientApplication> findBySubmittedEquals(Boolean submitted, Pageable pageable);
    Page<PatientApplication> findByPrescriberEmailAndSubmittedEquals(String email, Boolean submitted, Pageable pageable);
}
