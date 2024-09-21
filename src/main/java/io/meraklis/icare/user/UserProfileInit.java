package io.meraklis.icare.user;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.meraklis.icare.user.Role.*;

@Component
public class UserProfileInit {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @PostConstruct
    public void init() {
        userProfileRepository.deleteByEmail("evangelos@poneres.com");
        UserProfile evangelos = UserProfile.builder()
                .email("evangelos@poneres.com")
                .type(LANDLORD)
                .email("evangelos@poneres.com")
                .firstName("Evangelos")
                .lastName("Poneres")
                .authProviderId("auth0|66de56fc073c3dfd30f0cbad")
                .build();
        userProfileRepository.save(evangelos);

        userProfileRepository.deleteByEmail("poneres.c@gmail.com");
        UserProfile christos = UserProfile.builder()
                .email("poneres.c@gmail.com")
                .type(LANDLORD)
                .email("poneres.c@gmail.com")
                .firstName("Christos")
                .lastName("Poneres")
                .authProviderId("auth0|66de56e37571c397a84dc9e3")
                .build();
        userProfileRepository.save(christos);
    }
}
