package io.meraklis.icare.top_flight.aircraft;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Data
public class AircraftAvailability {
    @Id
    private Integer id;
    private Aircraft aircraft;
    private LocalDate start;
    private LocalDate end;
}
