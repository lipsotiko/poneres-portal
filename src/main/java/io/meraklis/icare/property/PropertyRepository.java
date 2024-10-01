package io.meraklis.icare.property;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PropertyRepository extends MongoRepository<Property, String> {
    Page<Property> findByCreatedBy(String userId, Pageable pageable);
}
