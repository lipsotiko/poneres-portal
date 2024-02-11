package io.meraklis.icare.security;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public interface AuthenticationService {
    OidcUser getPrincipal();
}
