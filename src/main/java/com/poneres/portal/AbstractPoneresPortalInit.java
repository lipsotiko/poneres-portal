package com.poneres.portal;

import com.poneres.portal.invoices.InvoiceJob;
import com.poneres.portal.invoices.InvoiceJobRepository;
import com.poneres.portal.pdfs.processors.PdfType;
import com.poneres.portal.user.UserProfile;
import com.poneres.portal.user.UserProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.poneres.portal.user.Role.*;

@Slf4j
@Service
public class AbstractPoneresPortalInit {
    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private InvoiceJobRepository invoiceJobRepository;

    public void init() {
        log.info("Default Poneres Portal Initialization");
        userProfileRepository.deleteById("1");
        UserProfile evangelos = UserProfile.builder()
                .id("1")
                .roles(Arrays.asList(ADMIN, OWNER, PROVIDER))
                .email("evangelos@poneres.com")
                .firstName("Evangelos")
                .lastName("DON'T DELETE")
                .phoneNumber("(443) 555-1234")
                .authProviderId("auth0|66fec30d5c7496d1c5f4a2da")
                .paymentProviderId("acct_1Q5rgHDCtswgh7ou")
                .build();
        userProfileRepository.save(evangelos);

        userProfileRepository.deleteByEmail("2");
        UserProfile christos = UserProfile.builder()
                .id("2")
                .roles(Arrays.asList(ADMIN, OWNER, PROVIDER))
                .email("poneres.c@gmail.com")
                .firstName("Christos")
                .lastName("DON'T DELETE")
                .phoneNumber("(443) 555-1234")
                .authProviderId("auth0|66de56e37571c397a84dc9e3")
                .paymentProviderId("acct_1Q5VZYRebMKd18Dx")
                .build();
        userProfileRepository.save(christos);

        userProfileRepository.deleteByEmail("3");
        UserProfile tenant = UserProfile.builder()
                .id("3")
                .roles(List.of(ADMIN, PROVIDER))
                .email("tenant@hello.io")
                .firstName("Tenant")
                .lastName("DON'T DELETE")
                .phoneNumber("(443) 301-9719")
                .authProviderId("auth0|67dca7b9760d7cbe9aa8a46d")
                .paymentProviderId(null)
                .build();
        userProfileRepository.save(tenant);

        invoiceJobRepository.deleteAll();
        invoiceJobRepository.save(InvoiceJob.builder()
                        .id("1")
                        .type(PdfType.KW_COMMISSION_INVOICE)
                        .jobMetadata(new HashMap<>(){{
                            put("client", "Sara");
                            put("to", "sara.garlisch@gmail.com");
                            put("cc", "eponeres@kw.com");
                            put("services", "Property Management");
                        }})
                        .pdfMetadata(new HashMap<>(){{
                            put("client", "Sara Garlisch");
                            put("address", "1761 W. Morse Ave #3N, Chicago, IL 60626");
                            put("services", "Property Management");
                            put("commission", "$188.65");
                        }})
                        .scheduleEnabled(true)
                .build());
        invoiceJobRepository.save(InvoiceJob.builder()
                .id("2")
                .type(PdfType.KW_COMMISSION_INVOICE)
                .jobMetadata(new HashMap<>(){{
                    put("client", "Veer");
                    put("to", "veervisaria1@gmail.com");
                    put("cc", "eponeres@kw.com");
                    put("services", "Tenant Placement");
                }})
                .pdfMetadata(new HashMap<>(){{
                    put("client", "Veer Visaria");
                    put("address", "1761 W. Morse Ave #GS, Chicago, IL 60626");
                    put("services", "Tenant Placement");
                    put("commission", "$2895.00");
                }})
                .scheduleEnabled(false)
                .build());
        invoiceJobRepository.save(InvoiceJob.builder()
                .id("3")
                .type(PdfType.KW_COMMISSION_INVOICE)
                .jobMetadata(new HashMap<>(){{
                    put("client", "Veer");
                    put("to", "veervisaria1@gmail.com");
                    put("cc", "eponeres@kw.com");
                    put("services", "Property Management");
                }})
                .pdfMetadata(new HashMap<>(){{
                    put("client", "Veer Visaria");
                    put("address", "1761 W. Morse Ave #GS, Chicago, IL 60626");
                    put("services", "Property Management");
                    put("commission", "$231.60");
                }})
                .scheduleEnabled(false)
                .build());
    }
}
