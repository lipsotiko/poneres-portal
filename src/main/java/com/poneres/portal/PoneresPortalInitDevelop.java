package com.poneres.portal;

import com.poneres.portal.shifts.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Component
@Profile({"local", "local-no-auth", "development"})
public class PoneresPortalInitDevelop extends AbstractPoneresPortalInit {

    @Autowired
    private ShiftRepository shiftRepository;

    @PostConstruct
    public void init() {
        super.init();

        shiftRepository.deleteAll();

        Map<String, String> facilities = getStringStringMap();

        LocalDate date = LocalDate.now().plusDays(1);
        for (int i = 1; i <= 25; i++) {

            for (Map.Entry<String, String> e : facilities.entrySet()) {
                String facility = e.getKey();
                String location = e.getValue();

                for (ShiftSpecialty specialty : ShiftSpecialty.values()) {
                    shiftRepository.save(Shift.builder()
                            .specialty(specialty)
                            .facility(facility)
                            .location(location)
                            .startDate(date)
                            .endDate(date.plusDays(2))
                            .startTime(LocalTime.of(7, 0))
                            .endTime(LocalTime.of(11, 0))
                            .rate(123.0)
                            .duration(i % 2 == 0 ? ShiftDuration.LONG_TERM : ShiftDuration.SHORT_TERM)
                            .rateFrequency(ShiftRateFrequency.HOUR)
                            .build());
                }
            }

            date = date.plusDays(1);
        }
    }

    private static Map<String, String> getStringStringMap() {
        Map<String, String> facilities = new HashMap<>();
        facilities.put("UF Health Shands Hospital", "Gainesville, FL");
        facilities.put("North Florida Regional Medical Center", "Gainesville, FL");
        facilities.put("Jackson Memorial Hospital", "Miami, FL");
        facilities.put("Aventura Hospital", "Aventura, FL");
        facilities.put("Tampa General Hospital", "Tampa, FL");
        facilities.put("Winter Haven Hospital", "Winter Haven, FL");
        facilities.put("Broward Health Medical Center", "Fort Lauderdale, FL");
        facilities.put("Baptist Health South Florida", "Miami, FL");
        facilities.put("UF Health Jacksonville", "Jacksonville, FL");
        facilities.put("Cleveland Clinic Florida", "Weston, FL");
        facilities.put("Sarasota Memorial Hospital", "Sarasota, FL");
        facilities.put("St. Joseph’s Hospital", "Tampa, FL");
        return facilities;
    }
}


