package io.meraklis.icare.security.auth0;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.meraklis.icare.user.SignUp;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Auth0CreateUserRequest {
    private String email;
    private String givenName;
    private String familyName;
    private String password;
    private String name;
    private String connection = "Username-Password-Authentication";

    public Auth0CreateUserRequest(SignUp signUp) {
        email = signUp.getEmail();
        givenName = signUp.getFirstName();
        familyName = signUp.getLastName();
        password = signUp.getPassword();
        name = String.format("%s, %s", signUp.getLastName(), signUp.getFirstName());
    }
}
