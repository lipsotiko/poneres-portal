package com.poneres.portal.maintenance;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenanceRepository extends MongoRepository<Maintenance, String> {

}
