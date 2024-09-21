package io.meraklis.icare.security;

import io.meraklis.icare.user.SignUp;
import io.meraklis.icare.user.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
@Profile("develop")
public class DevelopmentAuthenticationService extends AbstractAuthenticationService implements AuthenticationService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Value("${mock-user.as}") String mockUserAs;

    @Override
    public OidcUser getPrincipal() {
        return new OidcUser() {
            @Override
            public Map<String, Object> getClaims() {
                Map<String, Object> claims = new HashMap<>();
                claims.put("email", mockUserAs);
                return claims;
            }

            @Override
            public OidcUserInfo getUserInfo() {
                return null;
            }

            @Override
            public OidcIdToken getIdToken() {
                return null;
            }

            @Override
            public Map<String, Object> getAttributes() {
                return null;
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }
        };
    }

    @Override
    public String getEmail() {
        return getPrincipal().getEmail();
    }

    @Override
    public void createUser(SignUp signUp) {
        System.out.println(signUp);
    }

    @Override
    public UserVerificationStatus verificationStatus() {
        return null;
    }

}
