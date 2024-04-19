package io.meraklis.icare.security;

import io.meraklis.icare.user.Role;
import io.meraklis.icare.user.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@Profile("production")
public class ProductionAuthenticationService extends AbstractAuthenticationService implements AuthenticationService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    public OidcUser getPrincipal() {
        return (OidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public String getEmail() {
        return getPrincipal().getEmail();
    }
}
