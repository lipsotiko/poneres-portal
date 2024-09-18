package io.meraklis.icare.top_flight.user;

import lombok.Data;

import java.util.List;

@Data
public class TopFlightUserUpdateRequest {
    private String firstName;
    private String lastName;
    private String callsign;
    private List<Certification> certifications;
}
