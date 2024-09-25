package io.meraklis.icare.user;

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

    @PostConstruct
    public void init() {
        userProfileRepository.deleteByEmail("evangelos@poneres.com");
        UserProfile evangelos = UserProfile.builder()
                .roles(Arrays.asList(ADMIN, LANDLORD))
                .email("evangelos@poneres.com")
                .firstName("Evangelos")
                .lastName("Poneres")
                .authProviderId("auth0|66de56fc073c3dfd30f0cbad")
                .build();
        userProfileRepository.save(evangelos);

        userProfileRepository.deleteByEmail("poneres.c@gmail.com");
        UserProfile christos = UserProfile.builder()
                .roles(Arrays.asList(ADMIN, LANDLORD))
                .email("poneres.c@gmail.com")
                .firstName("Christos")
                .lastName("Poneres")
                .authProviderId("auth0|66de56e37571c397a84dc9e3")
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
