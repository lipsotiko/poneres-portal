package io.meraklis.icare.security;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
@Service
@Profile("production")
public class ProductionAuthenticationService implements AuthenticationService {
    @Override
    public OidcUser getPrincipal() {
        return (OidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
