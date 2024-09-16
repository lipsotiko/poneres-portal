package io.meraklis.icare.user;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/sign-up")
public class SignUpViewController {

    @GetMapping(produces = "text/html")
    public byte[] signUp() throws IOException {
        return new ClassPathResource("public/sign-up/index.html").getInputStream().readAllBytes();
    }
}
