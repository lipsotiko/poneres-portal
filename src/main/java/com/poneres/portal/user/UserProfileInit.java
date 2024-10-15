package com.poneres.portal.user;

import com.poneres.portal.property.PropertyRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static com.poneres.portal.user.Role.*;

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
                .roles(Arrays.asList(ADMIN, PROPERTY_MANAGER))
                .email("evangelos@poneres.com")
                .firstName("Evangelos")
                .lastName("DON'T DELETE")
                .authProviderId("auth0|66fec30d5c7496d1c5f4a2da")
                .paymentProviderId("acct_1Q5rgHDCtswgh7ou")
                .build();
        userProfileRepository.save(evangelos);

        userProfileRepository.deleteByEmail("2");
        UserProfile christos = UserProfile.builder()
                .id("2")
                .roles(Arrays.asList(ADMIN, PROPERTY_MANAGER))
                .email("poneres.c@gmail.com")
                .firstName("Christos")
                .lastName("DON'T DELETE")
                .authProviderId("auth0|66de56e37571c397a84dc9e3")
                .paymentProviderId("acct_1Q5VZYRebMKd18Dx")
                .build();
        userProfileRepository.save(christos);

        initPrescriber("prescriber_a@poneres.com", "Saul", "Goodman");
        initPrescriber("prescriber_b@poneres.com", "Johnny", "Walker");
        initPrescriber("prescriber_c@poneres.com", "Leo", "Getz");
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
}
