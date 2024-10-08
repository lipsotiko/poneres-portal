package io.meraklis.icare.security;

import io.meraklis.icare.applications.PatientApplication;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static io.meraklis.icare.helpers.RestApiService.urlEncode;

@Slf4j
@Service
@Profile({"development", "local"})
public class Auth0AuthenticationService implements AuthenticationService {

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
    public String getEmail() {
        return getPrincipal().getEmail();
    }

    @Override
    public UserProfile createUser(SignUp signUp, Role role) {
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
                .roles(Collections.singletonList(role))
                .build();
        return userProfileRepository.save(userProfile);
    }

    @Override
    public boolean isVerified() {
        UserProfile userProfile = userProfileRepository.findByAuthProviderId(getAuthProviderId());
        String token = getAccessToken();
        String uri = audience + "users/" + urlEncode(userProfile.getAuthProviderId());
        Auth0UserResponse response = restApiService.get(uri, token, Auth0UserResponse.class);
        return response.getEmailVerified();
    }

    @Override
    public UserProfile getUserProfile() {
        return userProfileRepository.findByAuthProviderId(getAuthProviderId());
    }

    @Override
    public void sendVerificationEmail() {
        String token = getAccessToken();
        Map<String, String> request = new HashMap<>();
        request.put("user_id", getAuthProviderId());
        restApiService.post(audience + "jobs/verification-email", token, request);
    }

    @Override
    public void update(UserProfile userProfile) {
        String token = getAccessToken();
        String uri = audience + "users/" + urlEncode(userProfile.getAuthProviderId());
        Map<String, String> request = new HashMap<>();
        request.put("given_name", userProfile.getFirstName());
        request.put("family_name", userProfile.getLastName());
        request.put("name", String.format("%s, %s", userProfile.getLastName(), userProfile.getFirstName()));
        request.put("email", userProfile.getEmail());
        restApiService.patch(uri, token, request);
    }

    private String getAuthProviderId() {
        return (String) getPrincipal().getUserInfo().getClaims().get("sub");
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

    public OidcUser getPrincipal() {
        return (OidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public Boolean hasRole(Role role) {
        return getUserProfile().getRoles().contains(role);
    }

    public Boolean isAuthorized(PatientApplication application) {
        return hasRole(Role.ADMIN) || (!hasRole(Role.ADMIN) && getEmail().equals(application.getPrescriberEmail()));
    }

    @Override
    public void deleteAccount(UserProfile userProfile) {
        if (userProfile.getAuthProviderId() == null) {
            return;
        }
        String token = getAccessToken();
        String uri = audience + "users/" + urlEncode(userProfile.getAuthProviderId());
        restApiService.delete(uri, token);
    }
}
