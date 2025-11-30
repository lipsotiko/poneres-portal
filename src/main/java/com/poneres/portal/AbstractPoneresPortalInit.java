package com.poneres.portal;

import com.poneres.portal.user.UserProfile;
import com.poneres.portal.user.UserProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static com.poneres.portal.user.Role.*;

@Slf4j
@Service
public class AbstractPoneresPortalInit {
    @Autowired
    private UserProfileRepository userProfileRepository;

    public void init() {
        log.info("Default Poneres Portal Initialization");
        userProfileRepository.deleteById("1");
        UserProfile evangelos = UserProfile.builder()
                .id("1")
                .roles(Arrays.asList(ADMIN, USER))
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
                .roles(Arrays.asList(ADMIN, USER))
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
                .roles(List.of(ADMIN, USER))
                .email("tenant@hello.io")
                .firstName("User")
                .lastName("DON'T DELETE")
                .phoneNumber("(443) 301-9719")
                .authProviderId("auth0|67dca7b9760d7cbe9aa8a46d")
                .paymentProviderId(null)
                .build();
        userProfileRepository.save(tenant);
    }
}
