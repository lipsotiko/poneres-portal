package io.meraklis.icare.top_flight.aircraft;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// This would be a SQL Repository
interface AircraftAvailabilityRepository extends MongoRepository<AircraftAvailability, String> {
     List<AircraftAvailability> findAllByAircraftId(Integer aircraftId);
}
