package io.meraklis.icare.top_flight.schedule;

import io.meraklis.icare.top_flight.aircraft.AircraftAvailability;
import io.meraklis.icare.top_flight.instructor.InstructorAvailability;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static io.meraklis.icare.top_flight.user.UserHelpers.isInstructor;

@Tag(name = "Top Flight Scheduler")
@RestController
@RequestMapping("/api/top-flight/schedule")
public class FlightScheduleController {

    @Operation(summary = "Return flight schedule by ID")
    @GetMapping("/flights/{id}")
    private FlightSchedule getFlightSchedule(@RequestParam Integer flightId) {
        return null;
    }

    @Operation(summary = "Return current flights scheduled within date range")
    @GetMapping("/flights")
    public List<FlightSchedule> getFlightSchedule(@RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate) {

        return Collections.emptyList();
    }

    @Operation(summary = "Return aircraft availability within date range")
    @GetMapping("/aircraft/availability")
    public List<AircraftAvailability> getAirCraftAvailability(@RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate) {

        return Collections.emptyList();
    }

    @Operation(summary = "Return instructor availability within date range")
    @GetMapping("/instructor/availability")
    public List<InstructorAvailability> getInstructorAvailability(@RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate) {

        return Collections.emptyList();
    }

    // Rather than requiring the UI to derive the available time blocks using the 3 APIs above,
    // The availability could be derived here; but it would depend of the desired user journey: What is selected first?
//    public List<TimeBlocks> getAvailability(@RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate) {
//
//    }

    @Operation(summary = "Schedule a flight")
    @PostMapping("/flights")
    @Transactional
    public void scheduleFlight(@RequestBody FlightScheduleRequest request, @AuthenticationPrincipal UserDetails userDetails) {

        // Verify aircraft is available
        // If an instructor was requested, verify their availability

        // ^^ The above check won't be needed since the trigger will handle these scenarios,
        // but it also can't hurt since unit testing the business logic is a good idea and would additionally prevent scheduling conflicts

        // If a solo flight was requested, verify the pilots credentials
        // Verify the start is at the top of the hour, or at 30 minutes past
        // Verify the aircraft has enough capacity
        // Ensure the start - end is evenly divisible by 30 minutes.
        // If the requested flight conflicts with any of the existing schedule, a DB Trigger will throw an exception
    }

    @Operation(summary = "Cancel a flight")
    @PostMapping("/flights/cancel/{id}")
    @Transactional
    public void scheduleFlight(@RequestParam Integer flightId, @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        if (!userIsPartOfFlight(flightId, userDetails.getUsername())) {
            throw new Exception("Error");
        }

        if (isInstructor(userDetails.getUsername())) {
            // TODO: Full refund to student
            return;
        }

        // Assume the user is a student
        FlightSchedule flightSchedule = getFlightSchedule(flightId);
        if (flightSchedule.getStart().minusHours(24).isAfter(LocalDateTime.now())) {
            // TODO: Full refund
        } else {
            // TODO: partial refund
        }
    }

    private boolean userIsPartOfFlight(Integer flightId, String username) {
        // TODO: Query FlightSchedule to see if this user part of the flight roster
        return false;
    }
}
