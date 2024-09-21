package io.meraklis.icare.security;

import io.meraklis.icare.helpers.FileService;
import io.meraklis.icare.helpers.RestApiService;
import io.meraklis.icare.security.auth0.*;
import io.meraklis.icare.user.Role;
import io.meraklis.icare.user.SignUp;
import io.meraklis.icare.user.UserProfile;
import io.meraklis.icare.user.UserProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static io.meraklis.icare.helpers.RestApiService.urlEncode;

@Slf4j
@Service
@Profile("production")
public class ProductionAuthenticationService extends AbstractAuthenticationService implements AuthenticationService {

    @Value("${okta.oauth2.issuer}oauth/token")
    private String tokenUrl;

    @Value("${auth0.administration.client-id}")
    private String clientId;

    @Value("${auth0.administration.client-secret}")
    private String clientSecret;

    @Value("${okta.oauth2.issuer}api/v2/")
    private String audience;

    @Autowired
    private RestApiService restApiService;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserProfileRepository userProfileRepository;

    private static final String cachedTokenFilePath = "/AUTH0_ADMIN_TOKEN.json";

    @Override
    public OidcUser getPrincipal() {
        return (OidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public String getEmail() {
        return getPrincipal().getEmail();
    }

    @Override
    public void createUser(SignUp signUp) {
        if (userProfileRepository.existsByEmail(signUp.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You already have an account. Try signing in!");
        }

        String token = getAccessToken();
        Auth0CreateUserRequest request = new Auth0CreateUserRequest(signUp);
        Auth0CreateUserResponse response =
                restApiService.post(audience + "users", token, request, Auth0CreateUserResponse.class);

        UserProfile userProfile = UserProfile.builder()
                .email(signUp.getEmail())
                .firstName(signUp.getFirstName())
                .lastName(signUp.getLastName())
                .authProviderId(response.getUserId())
                .type(Role.LANDLORD)
                .build();
        userProfileRepository.save(userProfile);
    }

    @Override
    public UserVerificationStatus verificationStatus() {
        UserProfile userProfile = userProfileRepository.findByEmail(getEmail());
        String token = getAccessToken();
        String uri = audience + "users/" + urlEncode(userProfile.getAuthProviderId());
        Auth0UserResponse response = restApiService.get(uri, token, Auth0UserResponse.class);
        return new UserVerificationStatus(response.getEmailVerified());
    }

    private String getAccessToken() {
        Auth0TokenResponse auth0TokenResponse = fileService.readFile(cachedTokenFilePath);

        if (auth0TokenResponse != null && !auth0TokenResponse.isExpired()) {
            log.info("Auth0 access token found; not expired.");
            return auth0TokenResponse.authorization();
        }

        Auth0TokenResponse token = restApiService.post(
                tokenUrl, new Auth0TokenRequest(clientId, clientSecret, audience), Auth0TokenResponse.class);
        token.setTimestamp(LocalDateTime.now());

        fileService.writeToFile(cachedTokenFilePath, token);

        return token.authorization();
    }
}
