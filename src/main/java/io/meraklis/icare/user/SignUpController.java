package io.meraklis.icare.user;

import io.meraklis.icare.email.EmailService;
import io.meraklis.icare.payments.PaymentService;
import io.meraklis.icare.security.AuthenticationService;
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

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/landlord")
    public ResponseEntity<Void> signUpLandlord(@RequestBody SignUp signUp) {
        authenticationService.createUser(signUp);
        paymentService.createAccount(signUp);
        emailService.send(signUp.getEmail(), "Welcome to Property Pal", "email_templates/welcome.html");
        return ResponseEntity.ok().build();
    }
}
