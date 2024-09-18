package io.meraklis.icare.user;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/sign-up")
public class SignUpController {
    @PostMapping("/landlord")
    public SignUp signUpLandlord(@RequestBody SignUp signUp) {
        // TODO: Create user in Auth0
        // TODO: Create Stripe Connect account
        return signUp;
    }
}
