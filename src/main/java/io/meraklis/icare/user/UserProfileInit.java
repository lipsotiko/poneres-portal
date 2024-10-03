package io.meraklis.icare.user;

import io.meraklis.icare.property.Property;
import io.meraklis.icare.property.PropertyRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static io.meraklis.icare.user.Role.*;

@Component
public class UserProfileInit {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @PostConstruct
    public void init() {
        propertyRepository.deleteAll();

        userProfileRepository.deleteById("1");
        UserProfile evangelos = UserProfile.builder()
                .id("1")
                .roles(Arrays.asList(ADMIN, LANDLORD))
                .email("evangelos@poneres.com")
                .firstName("Evangelos")
                .lastName("Poneres")
                .authProviderId("auth0|66fec30d5c7496d1c5f4a2da")
                .paymentProviderId("acct_1Q5rgHDCtswgh7ou")
                .build();
        userProfileRepository.save(evangelos);

        initProperty(evangelos, "2707 Hunting Ridge Ct.", "Baldwin", "21013");
        initProperty(evangelos, "2307 Oakmont Rd.", "Fallston", "21047");

        userProfileRepository.deleteByEmail("2");
        UserProfile christos = UserProfile.builder()
                .id("2")
                .roles(Arrays.asList(ADMIN, LANDLORD))
                .email("poneres.c@gmail.com")
                .firstName("Christos")
                .lastName("Poneres")
                .authProviderId("auth0|66de56e37571c397a84dc9e3")
                .paymentProviderId("acct_1Q5VZYRebMKd18Dx")
                .build();
        userProfileRepository.save(christos);

        initProperty(christos, "1037 Wingate Ct.", "Bel Air", "21014");
        initProperty(christos, "1041 Wingate Ct.", "Bel Air", "21014");

        initPrescriber("prescriber_a@poneres.com", "Saul", "Goodman");
        initPrescriber("prescriber_b@poneres.com", "Johnny", "Walker");
        initPrescriber("prescriber_c@poneres.com", "Leo", "Getz");

        initTenants();
    }

    private void initProperty(UserProfile userProfile, String address, String city, String zipCode) {
        for (int i = 0; i < 50; i++) {
            propertyRepository.save(Property.builder()
                    .address(address + " " + i + 1)
                    .city(city)
                    .state("MD")
                    .zipCode(zipCode)
                    .createdBy(userProfile.getId())
                    .build());
        }
    }

    private void initPrescriber(String email, String firstNane, String lastName) {
        userProfileRepository.deleteByEmail(email);
        UserProfile prescriberB = UserProfile.builder()
                .roles(List.of(PRESCRIBER))
                .email(email)
                .firstName(firstNane)
                .lastName(lastName)
                .build();
        userProfileRepository.save(prescriberB);
    }

    private void initTenants() {
        userProfileRepository.deleteByRolesContains(Role.TENANT);
        for (int i = 0; i < 50; i++) {
            String tenant = String.format("tenant_%s", i + 1);
            userProfileRepository.save(UserProfile.builder()
                    .email(tenant + "@example.com")
                    .firstName(tenant)
                    .lastName("Wick")
                    .roles(List.of(TENANT))
                    .build());
        }
    }
}
