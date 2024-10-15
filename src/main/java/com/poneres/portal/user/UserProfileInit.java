package com.poneres.portal.user;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.poneres.portal.user.Role.*;

@Component
public class UserProfileInit {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @PostConstruct
    public void init() {
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
    }
}
