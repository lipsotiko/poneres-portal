package com.poneres.portal.contact;

import com.poneres.portal.email.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/public/contact")
public class ContactRequestController {

    @Value("${email.poneres.to}")
    private String poneresTo;

    @Value("${email.xeniteas.to}")
    private String xeniteasTo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ContactRequestRepository contactRequestRepository;

    @PostMapping(value = "/request", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> request(
            ContactRequest contactRequest) {
        contactRequestRepository.save(contactRequest);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("FIRST_NAME", contactRequest.getFirstName());
        tokens.put("LAST_NAME", contactRequest.getLastName());
        tokens.put("EMAIL", contactRequest.getEmail());
        tokens.put("PHONE", contactRequest.getPhone());
        tokens.put("MESSAGE", contactRequest.getMessage());

        emailService.send(
                poneresTo,
                "Poneres.com: Request to be contacted",
                "request-to-be-contacted.html",
                tokens);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/get/requests")
    public Page<ContactRequest> getRequests(Pageable pageable) {
        return contactRequestRepository.findAll(pageable);
    }
}
