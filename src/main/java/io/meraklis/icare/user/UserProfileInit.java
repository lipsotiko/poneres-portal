package io.meraklis.icare.user;

import io.meraklis.icare.entities.Entity;
import io.meraklis.icare.entities.EntityRepository;
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
    private EntityRepository entityRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @PostConstruct
    public void init() {
        entityRepository.deleteAll();
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

        Entity everGreen = initEntity("Evergreen Apartments", "Joe", "Green", "123 S. Tough Ln.", "Chicago", "IL", "60600", "contact@evergreen.com");
        initProperty(everGreen, "2707 Hunting Ridge Ct.", "Baldwin", "21013");
        initProperty(everGreen, "2307 Oakmont Rd.", "Fallston", "21047");

        Entity axeRealty = initEntity("Axe Realty", "Steve", "White", "333 S. Stuff Ln.", "Miami", "FL", "30600", "contact@axe.com");
        initProperty(axeRealty, "1037 Wingate Ct.", "Bel Air", "21014");
        initProperty(axeRealty, "1041 Wingate Ct.", "Bel Air", "21014");

        initPrescriber("prescriber_a@poneres.com", "Saul", "Goodman");
        initPrescriber("prescriber_b@poneres.com", "Johnny", "Walker");
        initPrescriber("prescriber_c@poneres.com", "Leo", "Getz");

        initTenants();
    }

    private Entity initEntity(String name, String firstName, String lastName, String address, String city, String state, String zipCode, String email) {
        return entityRepository.save(Entity.builder()
                .name(name)
                .contactFirstName(firstName)
                .contactLastName(lastName)
                .contactAddress(address)
                .contactCity(city)
                .contactState(state)
                .contactZipCode(zipCode)
                .contactEmail(email)
                .build());
    }

    private void initProperty(Entity entity, String address, String city, String zipCode) {
        for (int i = 0; i < 50; i++) {
            propertyRepository.save(Property.builder()
                    .address(address + " " + i + 1)
                    .city(city)
                    .state("MD")
                    .zipCode(zipCode)
                    .entityId(entity.getId())
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
