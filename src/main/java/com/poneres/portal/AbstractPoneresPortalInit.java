package com.poneres.portal;

import com.poneres.portal.user.UserProfile;
import com.poneres.portal.user.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static com.poneres.portal.user.Role.*;

@Service
public class AbstractPoneresPortalInit {
    @Autowired
    private UserProfileRepository userProfileRepository;

    public void init() {
        userProfileRepository.deleteById("1");
        UserProfile evangelos = UserProfile.builder()
                .id("1")
                .roles(Arrays.asList(ADMIN, OWNER, PROVIDER))
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
                .roles(Arrays.asList(ADMIN, OWNER, PROVIDER))
                .email("poneres.c@gmail.com")
                .firstName("Christos")
                .lastName("DON'T DELETE")
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
                .authProviderId("auth0|67dca7b9760d7cbe9aa8a46d")
                .paymentProviderId(null)
                .build();
        userProfileRepository.save(tenant);
    }
}
