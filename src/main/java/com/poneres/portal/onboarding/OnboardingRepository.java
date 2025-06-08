package com.poneres.portal.onboarding;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnboardingRepository extends MongoRepository<Onboarding, String> {

}
