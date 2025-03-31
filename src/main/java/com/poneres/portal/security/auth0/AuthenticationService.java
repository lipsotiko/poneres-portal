package com.poneres.portal.security.auth0;

import com.poneres.portal.user.Role;
import com.poneres.portal.user.SignUp;
import com.poneres.portal.user.UserProfile;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public interface AuthenticationService {
    OidcUser getPrincipal();

    String getEmail();

    Boolean hasRole(Role role);

    UserProfile createUser(SignUp signUp);

    boolean isVerified();

    UserProfile getUserProfile();

    void sendVerificationEmail();

    void update(UserProfile userProfile);

    void deleteAccount(UserProfile userProfile);
}
