package com.poneres.portal.agreements;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgreementRepository extends MongoRepository<Agreement, String> {
}
