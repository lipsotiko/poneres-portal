package io.meraklis.icare.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/sign-up")
public class SignUpController {
    @GetMapping("/user")
    public String signUp() {
        return "Hello";
    }
}
