package io.meraklis.icare.top_flight.instructor;

import io.meraklis.icare.top_flight.user.TopFlightUser;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Data
public class InstructorAvailability {
    @Id
    private Integer id;
    private TopFlightUser instructor;
    private LocalDate start;
    private LocalDate end;
}
