package io.meraklis.icare.top_flight.aircraft;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Aircraft {
    @Id
    private Integer id;
}
