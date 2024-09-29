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

        userProfileRepository.deleteByEmail("evangelos@poneres.com");
        UserProfile evangelos = UserProfile.builder()
                .roles(Arrays.asList(ADMIN, LANDLORD))
                .email("evangelos@poneres.com")
                .firstName("Evangelos")
                .lastName("Poneres")
                .authProviderId("auth0|66de56fc073c3dfd30f0cbad")
                .build();
        userProfileRepository.save(evangelos);

        initProperty(evangelos, "2707 Hunting Ridge Ct.", "Baldwin", "21013");
        initProperty(evangelos, "2307 Oakmont Rd.", "Fallston", "21047");

        userProfileRepository.deleteByEmail("poneres.c@gmail.com");
        UserProfile christos = UserProfile.builder()
                .roles(Arrays.asList(ADMIN, LANDLORD))
                .email("poneres.c@gmail.com")
                .firstName("Christos")
                .lastName("Poneres")
                .authProviderId("auth0|66de56e37571c397a84dc9e3")
                .build();
        userProfileRepository.save(christos);

        initProperty(christos, "1037 Wingate Ct.", "Bel Air", "21014");
        initProperty(christos, "1041 Wingate Ct.", "Bel Air", "21014");

        initPrescriber("prescriber_a@poneres.com", "Saul", "Goodman");
        initPrescriber("prescriber_b@poneres.com", "Johnny", "Walker");
        initPrescriber("prescriber_c@poneres.com", "Leo", "Getz");
    }

    private void initProperty(UserProfile userProfile, String address, String city, String zipCode) {
        propertyRepository.save(Property.builder()
                .address(address)
                .city(city)
                .state("MD")
                .zipCode(zipCode)
                .createdBy(userProfile.getId())
                .build());
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
