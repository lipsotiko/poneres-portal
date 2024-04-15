package io.meraklis.icare.signatures;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SignatureRepository extends MongoRepository<Signature, String> {
    List<Signature> findByType(SignatureType signatureType);
}
