package io.meraklis.icare.user;

import io.meraklis.icare.authentication.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/info")
    public Map<String, Object> home() {
        return authenticationService.getPrincipal().getClaims();
    }
}
