package io.meraklis.icare.top_flight.schedule;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
public class FlightScheduleRequest {
    @NonNull
    private Integer aircraftId;
    private Integer instructorId;
    private LocalDateTime start;
    private LocalDateTime end;
    private Boolean solo;
    private String notes;
}
