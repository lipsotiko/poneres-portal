package io.meraklis.icare.agreements;

import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/agreements")
public class ManagementAgreementController {

    @GetMapping("/{id}")
    public ManagementAgreement get(@Param("id") String agreementId) {
        // TODO: introduce one time use and expiration...
        System.out.println(agreementId);
        return ManagementAgreement.builder().build();
    }
}
