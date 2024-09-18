package io.meraklis.icare.top_flight.schedule;

import io.meraklis.icare.top_flight.user.TopFlightUser;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FlightSchedule {
    @Id
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
    int id;
    @Version
    int version;
    private LocalDateTime start;
    private List<TopFlightUser> roster;
    public Boolean isSolo() {
        return roster.size() == 1;
    }
}
