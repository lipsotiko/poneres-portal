package io.meraklis.icare.applications;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "patient-applications")
public interface PatientApplicationRepository extends MongoRepository<PatientApplication, String> {
}
