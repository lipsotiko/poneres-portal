package com.poneres.portal.contact;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRequestRepository extends MongoRepository<ContactRequest, String> {
}
