package com.poneres.portal.property;

import com.poneres.portal.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    @Value("${webapp.url}")
    private String webappUrl;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private PropertyInvitationRepository propertyInvitationRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping
    public Property save(@RequestBody Property property) {
        return propertyRepository.save(property);
    }

    @GetMapping
    public Page<Property> get(Pageable pageable) {
        return propertyRepository.findAll(pageable);
    }

    @PostMapping("{propertyId}/invite-to-apply")
    public void inviteToApply(
            @PathVariable("propertyId") String propertyId,
            @RequestBody InviteToApplyRequest inviteToApplyRequest) {
        String email = inviteToApplyRequest.getEmail();
        propertyRepository.findById(propertyId).ifPresent(property -> {
            Map<String, String> additionalTokens = new HashMap<>();
            additionalTokens.put("PROPERTY_ADDRESS", property.getFullAddress());
            additionalTokens.put("APPLICATION_URL", webappUrl + "/apply/" + property.getId());
            emailService.send(email, "You've been invited to apply!", "invite-to-apply.html", additionalTokens);
            propertyInvitationRepository.save(
                    PropertyInvitation.builder()
                            .email(email)
                            .propertyId(property.getId())
                            .createdAt(LocalDateTime.now())
                            .build()
            );
        });
    }
}
