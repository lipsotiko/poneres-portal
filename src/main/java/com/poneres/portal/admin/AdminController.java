package com.poneres.portal.admin;

import com.poneres.portal.payments.PaymentService;
import com.poneres.portal.security.auth0.AuthenticationService;
import com.poneres.portal.security.auth0.UserAuthorized;
import com.poneres.portal.user.UserProfile;
import com.poneres.portal.user.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PaymentService paymentService;

    @DeleteMapping("/users")
    @UserAuthorized("isAdmin")
    public void deleteUsers(@RequestBody AdminDeleteUserRequest adminDeleteUserRequest) {
        List<String> idsToDelete = adminDeleteUserRequest.getUserIds().stream()
                .filter(id -> !List.of("1", "2", "3").contains(id)).collect(Collectors.toList());
        List<UserProfile> userProfiles = userProfileRepository.findAllById(idsToDelete);
        userProfiles.forEach(userProfile -> {
            paymentService.deleteAccount(userProfile);
            authenticationService.deleteAccount(userProfile);
        });

        userProfileRepository.deleteAllById(idsToDelete);
    }
}
