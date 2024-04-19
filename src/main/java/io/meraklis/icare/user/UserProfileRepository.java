package io.meraklis.icare.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProfileRepository extends MongoRepository<UserProfile, String> {
    UserProfile findByEmail(String email);

    List<UserProfile> findByType(Role type);
}
