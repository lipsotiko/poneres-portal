package com.poneres.portal.maintenance;

import com.poneres.portal.security.auth0.UserAuthorized;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {

    @PostMapping
    @UserAuthorized(value = { "isAdmin", "isTenant"})
    public void save(@RequestBody MaintenanceRequest request) {
        System.out.println(request);
    }

}
