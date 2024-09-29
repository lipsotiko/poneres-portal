package io.meraklis.icare.property;

import io.meraklis.icare.security.AuthenticationService;
import io.meraklis.icare.user.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping
    public Property save(@RequestBody Property property) {
        UserProfile userProfile = authenticationService.getUserProfile();
        property.setCreatedBy(userProfile.getId());
        return propertyRepository.save(property);
    }

    @GetMapping("/created-by")
    public List<Property> createdBy() {
        UserProfile userProfile = authenticationService.getUserProfile();
        return propertyRepository.findByCreatedBy(userProfile.getId());
    }

}
