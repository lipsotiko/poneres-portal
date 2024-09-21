package io.meraklis.icare.user;

import io.meraklis.icare.security.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/sign-up")
public class SignUpController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/landlord")
    public ResponseEntity<Void> signUpLandlord(@RequestBody SignUp signUp) {
        // TODO: Create Stripe Connect account
        authenticationService.createUser(signUp);
        return ResponseEntity.ok().build();
    }
}
