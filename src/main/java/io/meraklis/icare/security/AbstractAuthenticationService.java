package io.meraklis.icare.security;

import io.meraklis.icare.applications.PatientApplication;
import io.meraklis.icare.user.Role;
import io.meraklis.icare.user.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;

abstract class AbstractAuthenticationService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    abstract String getEmail();

    public Boolean hasRole(Role role) {
        return userProfileRepository.findByEmail(getEmail()).getRoles().contains(role);
    }

    public Boolean isAuthorized(PatientApplication application) {
        return hasRole(Role.ADMIN) || (!hasRole(Role.ADMIN) && getEmail().equals(application.getPrescriberEmail()));
    }
}
