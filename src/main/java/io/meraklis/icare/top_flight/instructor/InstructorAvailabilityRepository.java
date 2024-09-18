package io.meraklis.icare.top_flight.instructor;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// This would be a SQL Repository
interface InstructorAvailabilityRepository extends MongoRepository<InstructorAvailability, String> {
    List<InstructorAvailability> findAllByInstructorId(Integer instructorId);
}
