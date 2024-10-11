package com.poneres.portal.applications;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientApplicationRepository extends MongoRepository<PatientApplication, String> {
    Page<PatientApplication> findByCompleteTrue(Pageable page);

    Page<PatientApplication> findBySubmittedTrue(Pageable page);

    Page<PatientApplication> findByPrescriberEmailAndCompleteTrue(String email, Pageable page);

    Page<PatientApplication> findByPrescriberEmailAndSubmittedTrue(String email, Pageable page);

    Page<PatientApplication> findByCompleteFalseAndSubmittedFalse(Pageable page);

    Page<PatientApplication> findByPrescriberEmailAndCompleteFalseAndSubmittedFalse(String email, Pageable page);
}
