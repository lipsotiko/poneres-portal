package io.meraklis.icare.top_flight.user;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class TopFlightUser {
    @Id
    private Integer id;

    private List<Role> roles;

    public boolean hasRole(RoleType type) {
        return roles.stream().anyMatch(r -> r.getType() == type);
    }
}
