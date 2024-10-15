package com.poneres.portal.user;

import com.poneres.portal.email.EmailService;
import com.poneres.portal.security.auth0.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/sign-up")
public class SignUpController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private EmailService emailService;

//    @Autowired
//    private PaymentService paymentService;

    @PostMapping("/tenant")
    public ResponseEntity<Void> signUpLandlord(@RequestBody SignUp signUp) {
        authenticationService.createUser(signUp, Role.TENANT);
//        paymentService.createAccount(signUp);
        emailService.send(signUp.getEmail(), "Welcome to Poneres.com", "welcome-tenant.html");
        return ResponseEntity.ok().build();
    }
}
