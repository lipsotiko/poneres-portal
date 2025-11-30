package com.poneres.portal;

import com.poneres.portal.storage.StorageService;
import com.poneres.portal.user.UserProfile;
import com.poneres.portal.user.UserProfileRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.poneres.portal.user.Role.USER;

@Slf4j
@Component
@Profile({"local", "local-no-auth"})
public class PoneresPortalInitLocal extends AbstractPoneresPortalInit {

    @Autowired
    private StorageService storageService;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @PostConstruct
    public void init() {
        super.init();

        log.info("Local Poneres Portal Initialization");

        for(int i = 1; i <= 100; i++) {
            String id = String.format("TEST_USER_%s", i);
            userProfileRepository.deleteById(id);
            UserProfile testUser = UserProfile.builder()
                    .id(id)
                    .roles(List.of(USER))
                    .email("test_user" + id + "@cool.io")
                    .firstName("Test")
                    .lastName("User " + id)
                    .phoneNumber("(443) 555-1234")
                    .build();
            userProfileRepository.save(testUser);
        }

    }
}
