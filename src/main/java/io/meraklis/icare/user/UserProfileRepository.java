package io.meraklis.icare.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProfileRepository extends MongoRepository<UserProfile, String> {
    Boolean existsByEmail(String email);

    UserProfile findByEmail(String email);

    List<UserProfile> findByRolesContains(Role role);

    void deleteByEmail(String email);
}
