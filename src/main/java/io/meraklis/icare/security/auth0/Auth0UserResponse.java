package io.meraklis.icare.security.auth0;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Auth0UserResponse {
    private String userId;
    private String email;
    private Boolean emailVerified;
}
