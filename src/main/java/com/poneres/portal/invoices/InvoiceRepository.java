package com.poneres.portal.invoices;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends MongoRepository<Invoice, String> {
}
