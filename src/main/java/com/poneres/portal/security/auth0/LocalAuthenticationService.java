package com.poneres.portal.security.auth0;

import com.poneres.portal.user.Role;
import com.poneres.portal.user.SignUp;
import com.poneres.portal.user.UserProfile;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@Profile("local-no-auth")
public class LocalAuthenticationService implements AuthenticationService {
    @Override
    public OidcUser getPrincipal() {
        return null;
    }

    @Override
    public String getEmail() {
        return "";
    }

    @Override
    public Boolean hasRole(Role role) {
        return true;
    }

    @Override
    public UserProfile createUser(SignUp signUp, Role role) {
        return null;
    }

    @Override
    public boolean isVerified() {
        return false;
    }

    @Override
    public UserProfile getUserProfile() {
        return null;
    }

    @Override
    public void sendVerificationEmail() {

    }

    @Override
    public void update(UserProfile userProfile) {

    }

    @Override
    public void deleteAccount(UserProfile userProfile) {

    }
}
