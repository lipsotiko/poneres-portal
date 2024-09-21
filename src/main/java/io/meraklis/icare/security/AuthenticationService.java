package io.meraklis.icare.security;

import io.meraklis.icare.applications.PatientApplication;
import io.meraklis.icare.user.Role;
import io.meraklis.icare.user.SignUp;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public interface AuthenticationService {
    OidcUser getPrincipal();

    String getEmail();

    Boolean hasRole(Role role);

    Boolean isAuthorized(PatientApplication application);

    void createUser(SignUp signUp);

    UserVerificationStatus verificationStatus();
}
