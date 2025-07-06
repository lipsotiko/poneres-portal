package com.poneres.portal.shifts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/public/shifts")
public class PublicShiftController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ShiftRepository shiftRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<Shift> getPage(
            @Param("location") String location,
            @Param("specialty") String specialty,
            @Param("duration") String duration,
            @Param("search") String search,
            Pageable pageable) {
        Query query = new Query().with(pageable);

        List<Criteria> criteriaList = new ArrayList<>();
        if (location != null) {
            criteriaList.add(Criteria.where("location").is(location));
        }

        if (specialty != null) {
            ShiftSpecialty shiftSpecialty = ShiftSpecialty.valueOf(specialty);
            criteriaList.add(Criteria.where("specialty").is(shiftSpecialty));
        }

        if (duration != null) {
            ShiftDuration shiftDuration = ShiftDuration.valueOf(duration);
            criteriaList.add(Criteria.where("duration").is(shiftDuration));
        }

        if (search != null) {
            criteriaList.add(Criteria.where("facility").regex(search));
        }

        if (criteriaList.isEmpty()) {
            return shiftRepository.findAll(pageable);
        }

        Criteria criteria = new Criteria();
        query.addCriteria(criteria.andOperator(criteriaList));

        List<Shift> shifts = mongoTemplate.find(query, Shift.class);
        return PageableExecutionUtils.getPage(shifts, pageable, () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Shift.class));
    }
}
