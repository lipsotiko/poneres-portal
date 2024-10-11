package com.poneres.portal.security.auth0.auth0;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Auth0TokenRequest {
    private String clientId;
    private String clientSecret;
    private String audience;
    private String grant_type = "client_credentials";

    public Auth0TokenRequest(String clientId, String clientSecret, String audience) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.audience = audience;
    }
}
