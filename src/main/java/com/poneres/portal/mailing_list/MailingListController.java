package com.poneres.portal.mailing_list;

import com.poneres.portal.email.EmailService;
import com.poneres.portal.user.Organization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/public/mailing-list")
public class MailingListController {

    @Value("${email.xeniteas.no-reply}")
    private String xeniteasNoReply;

    @Value("${email.xeniteas.to}")
    private String xeniteasTo;

    @Value("${email.poneres.no-reply}")
    private String poneresNoReply;

    @Value("${email.poneres.to}")
    private String poneresTo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MailingListRepository mailingListRepository;

    @PostMapping(value = "/join", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> joinMailingList(
            @Param("organization") Organization organization,
            MailingListJoinRequest mailingListJoinRequest) {
        String email = mailingListJoinRequest.getEmail();

        if (mailingListRepository.existsByOrganizationAndEmail(organization, email)) {
            log.info("Mailing list Attempt to sign up again for organization {} and email {}", organization, email);
            return ResponseEntity.ok().build();
        }

        mailingListRepository.save(mailingListJoinRequest);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("FIRST_NAME", mailingListJoinRequest.getFirstName());
        tokens.put("LAST_NAME", mailingListJoinRequest.getLastName());
        tokens.put("EMAIL", email);

        if (organization.equals(Organization.XENITEAS)) {
                emailService.send(
                        xeniteasNoReply,
                        xeniteasTo,
                        "Xeniteas: Request to join mailing list",
                        "request-to-join-mailing-list.html",
                        tokens);
        } else if (organization.equals(Organization.PONERES)) {
            emailService.send(
                    poneresNoReply,
                    poneresTo,
                    "Poneres.com: Request to join mailing list",
                    "request-to-join-mailing-list.html",
                    tokens);
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/get/requests")
    public MailingList getRequests(@Param("organization") Organization organization) {
        List<MailingListJoinRequest> byOrganization = mailingListRepository.findByOrganization(organization);
        return new MailingList(byOrganization);
    }
}
