package io.meraklis.icare.security;

import io.meraklis.icare.applications.PatientApplication;
import io.meraklis.icare.user.Role;
import io.meraklis.icare.user.SignUp;
import io.meraklis.icare.user.UserProfile;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public interface AuthenticationService {
    OidcUser getPrincipal();

    String getEmail();

    Boolean hasRole(Role role);

    void createUser(SignUp signUp);

    boolean isVerified();

    UserProfile getUserProfile();

    void sendVerificationEmail();

    void update(UserProfile userProfile);

    Boolean isAuthorized(PatientApplication application);
}
