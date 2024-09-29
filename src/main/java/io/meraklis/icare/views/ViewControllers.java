package io.meraklis.icare.views;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ViewControllers {

    @GetMapping(path = "/sign-up", produces = "text/html")
    public byte[] signUp() throws IOException {
        return new ClassPathResource("public/sign-up/index.html").getInputStream().readAllBytes();
    }

    @GetMapping(path = "/earnings/payout-method", produces = "text/html")
    public byte[] payoutMethod() throws IOException {
        return new ClassPathResource("public/earnings/payout-method/index.html").getInputStream().readAllBytes();
    }
}
