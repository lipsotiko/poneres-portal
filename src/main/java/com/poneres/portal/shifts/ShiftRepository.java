package com.poneres.portal.shifts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftRepository extends MongoRepository<Shift, String> {

    Page<Shift> findByLocation(String location, Pageable pageable);

    Page<Shift> findBySpecialty(ShiftSpecialty specialty, Pageable pageable);

    Page<Shift> findByLocationAndSpecialty(String location, ShiftSpecialty shiftSpecialty, Pageable pageable);

    Page<Shift> findByLocationAndSpecialtyAndDuration(String location, ShiftSpecialty shiftSpecialty, ShiftDuration duration, Pageable pageable);
}
