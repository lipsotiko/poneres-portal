package io.meraklis.icare.security;

import io.meraklis.icare.applications.PatientApplication;
import io.meraklis.icare.user.Role;
import io.meraklis.icare.user.SignUp;
import io.meraklis.icare.user.UserProfile;
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
    public Boolean isAuthorized(PatientApplication application) {
        return null;
    }

    @Override
    public void deleteAccount(UserProfile userProfile) {

    }
}
