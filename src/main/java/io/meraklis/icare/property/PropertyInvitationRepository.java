package io.meraklis.icare.property;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyInvitationRepository extends MongoRepository<PropertyInvitation, String> {
}
