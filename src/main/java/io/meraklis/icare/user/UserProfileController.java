package io.meraklis.icare.user;

import io.meraklis.icare.security.UserAuthorized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user-profile")
public class UserProfileController {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @UserAuthorized("isAdmin")
    @GetMapping("/prescribers")
    public List<UserProfile> getPrescribers() {
        return userProfileRepository.findByType(Role.PRESCRIBER);
    }
}
