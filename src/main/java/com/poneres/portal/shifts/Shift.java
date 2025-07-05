package com.poneres.portal.shifts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shift {
    @Id
    private String id;
    private ShiftSpecialty specialty;
    private String facility;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Double rate;
    private ShiftDuration duration;
    private ShiftRateFrequency rateFrequency;
    private LocalDateTime createdAt;
}
