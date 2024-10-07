package io.meraklis.icare.entities;

import io.meraklis.icare.email.EmailService;
import io.meraklis.icare.entities.agreements.ManagementAgreement;
import io.meraklis.icare.entities.agreements.ManagementAgreementRepository;
import io.meraklis.icare.security.AuthenticationService;
import io.meraklis.icare.user.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/entities")
public class EntityController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private EntityRepository entityRepository;

    @Autowired
    private ManagementAgreementRepository managementAgreementRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public Page<Entity> get(Pageable pageable) {
        return entityRepository.findAll(pageable);
    }

    @PostMapping
    public Entity save(@RequestBody Entity entity) {
        UserProfile userProfile = authenticationService.getUserProfile();
        entity.setCreatedBy(userProfile.getId());
        Entity savedEntity = entityRepository.save(entity);
        ManagementAgreement managementAgreement = managementAgreementRepository.save(ManagementAgreement.builder().entityId(entity.getId()).build());
        Map<String, String> tokens = new HashMap<>();
        tokens.put("AGREEMENT_PATH", "agreements/" + managementAgreement.getId());
        emailService.send(savedEntity.getContactEmail(), "Signature Request for Property Management Agreement", "management-agreement.html", tokens);
        return savedEntity;
    }
}
