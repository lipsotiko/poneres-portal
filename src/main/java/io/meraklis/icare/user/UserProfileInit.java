package io.meraklis.icare.user;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.meraklis.icare.user.Role.ADMIN;
import static io.meraklis.icare.user.Role.PRESCRIBER;

@Component
public class UserProfileInit {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @PostConstruct
    public void init() {
        userProfileRepository.deleteAll();
        userProfileRepository.save(new UserProfile(ADMIN, "evangelos@poneres.com", "E", "Poneres"));
        userProfileRepository.save(new UserProfile(ADMIN, "poneres.c@gmail.com", "C", "Poneres"));
        userProfileRepository.save(new UserProfile(PRESCRIBER, "prescriber@meraklis.io", "Saul", "Goodman"));
        userProfileRepository.save(new UserProfile(PRESCRIBER, "prescriber_2@meraklis.io", "Jessy", "Pinkman"));
        userProfileRepository.save(new UserProfile(PRESCRIBER, "prescriber_3@meraklis.io", "Walter", "White"));
    }
}
