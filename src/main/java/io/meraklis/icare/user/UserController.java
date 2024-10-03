package io.meraklis.icare.user;

import io.meraklis.icare.payments.PaymentAccount;
import io.meraklis.icare.payments.PaymentAccountLink;
import io.meraklis.icare.payments.PaymentService;
import io.meraklis.icare.security.AuthenticationService;
import io.meraklis.icare.security.UserAuthorized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public Page<UserProfile> getUsers(Pageable pageable) {
        return userProfileRepository.findAll(pageable);
    }

    @GetMapping("/info")
    public Map<String, Object> home() {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("verified", authenticationService.isVerified());
        userInfo.put("userProfile", authenticationService.getUserProfile());
        return userInfo;
    }

    @PatchMapping("/{id}")
    public void updateUser(@PathVariable("id") String id, @RequestBody UpdateUserRequest updateUserRequest) {
        Optional<UserProfile> byId = userProfileRepository.findById(id);
        byId.ifPresent(userProfile -> {
            userProfile.setFirstName(updateUserRequest.getFirstName());
            userProfile.setLastName(updateUserRequest.getLastName());
            userProfile.setEmail(updateUserRequest.getEmail());
            userProfileRepository.save(userProfile);
            authenticationService.update(userProfile);
        });
    }

    @PostMapping("/send-verification-email")
    public void sendVerificationEmail() {
        authenticationService.sendVerificationEmail();
    }

    @GetMapping("/role")
    public UserProfile role() {
        return authenticationService.getUserProfile();
    }

    @UserAuthorized("isAdmin")
    @GetMapping("/prescribers")
    public List<UserProfile> getPrescribers() {
        return userProfileRepository.findByRolesContains(Role.PRESCRIBER);
    }

    @GetMapping("/payment-account")
    public PaymentAccount getPaymentAccount() {
        return paymentService.getAccount();
    }

    @GetMapping("/payment-account-link")
    public PaymentAccountLink getPaymentAccountOnboardingLink() {
        return paymentService.getAccountLink();
    }

}
