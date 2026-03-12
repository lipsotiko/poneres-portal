package com.poneres.portal.payments;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentIntentRepository extends MongoRepository<PaymentIntentRequested, String> {
    PaymentIntentRequested findByPaymentIntentId(String id);
}
