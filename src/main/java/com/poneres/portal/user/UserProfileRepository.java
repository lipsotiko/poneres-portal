package com.poneres.portal.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProfileRepository extends MongoRepository<UserProfile, String> {
    Boolean existsByEmail(String email);

    UserProfile findByAuthProviderId(String authProviderId);

    List<UserProfile> findByRolesContains(Role role);

    void deleteByEmail(String email);

    void deleteByRolesContains(Role role);

    UserProfile findByEmail(String email);
}
