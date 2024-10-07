package io.meraklis.icare.entities.agreements;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagementAgreementRepository extends MongoRepository<ManagementAgreement, String> {
}
