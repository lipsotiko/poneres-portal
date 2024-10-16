package com.poneres.portal;

import com.poneres.portal.agreements.AgreementRepository;
import com.poneres.portal.signatures.SignatureService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"local", "local-no-auth"})
public class PoneresPortalInitLocal extends AbstractPoneresPortalInit {

    @Autowired
    private AgreementRepository agreementRepository;

    @Autowired
    private SignatureService signatureService;

    @PostConstruct
    public void init() {
        super.init();
        agreementRepository.findAll().forEach(agreement -> {
            signatureService.delete(agreement.getSsdId());
            agreementRepository.delete(agreement);
        });
    }
}
