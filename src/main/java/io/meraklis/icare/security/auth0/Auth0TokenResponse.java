package io.meraklis.icare.security.auth0;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.beans.Transient;
import java.time.LocalDateTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Auth0TokenResponse {
    private String accessToken;
    private String scope;
    private Integer expiresIn; // seconds
    private String tokenType;
    private LocalDateTime timestamp;

    public String authorization() {
        return tokenType + " " + accessToken;
    }

    @Transient
    public boolean isExpired() {
        return timestamp == null || timestamp.plusSeconds(expiresIn).isBefore(LocalDateTime.now());
    }
}
