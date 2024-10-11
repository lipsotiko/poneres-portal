package com.poneres.portal.security.auth0.auth0;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Auth0CreateUserResponse {
    private String userId;
    private String email;
    private Boolean emailVerified;
}
