package com.poneres.portal.user;

import com.poneres.portal.email.EmailService;
import com.poneres.portal.payments.PaymentService;
import com.poneres.portal.security.auth0.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/public/sign-up")
public class SignUpController {

    @Value("${email.applications.to}")
    private String sendApplicationsTo;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/tenant")
    public ResponseEntity<Void> signUpLandlord(@RequestBody SignUp signUp) {
//        authenticationService.createUser(signUp, Role.TENANT);
//        paymentService.createAccount(signUp);
//        emailService.send(signUp.getEmail(), "Welcome to Poneres.com", "welcome-tenant.html");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/tenant/apply")
    public ResponseEntity<Void> signUpLandlord(@RequestBody Map<String, Object> applicationContent) {
        StringBuilder html = new StringBuilder();
        html.append("<table>");
        html.append("<thead><tr>");
        html.append("<th>Key</th>");
        html.append("<th>Value</th>");
        html.append("</tr></thead>");
        html.append("<tbody>");
        applicationContent.keySet().forEach((k) -> {
            html.append("<tr>");
            html.append("<td>");
            html.append(k);
            html.append("</td>");
            html.append("<td>");
            html.append(applicationContent.get(k));
            html.append("</td>");
            html.append("<tr>");
        });
        html.append("</tbody>");
        Map<String, String> tokens = new HashMap<>();
        tokens.put("APPLICATION_CONTENT", html.toString());

        String[] emails = sendApplicationsTo.split(",");
        for (String email : emails) {
            emailService.send(email, "A new application has been submitted", "new-application.html", tokens);
        }

        return ResponseEntity.ok().build();
    }
}
