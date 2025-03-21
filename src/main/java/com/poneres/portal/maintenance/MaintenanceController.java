package com.poneres.portal.maintenance;

import com.poneres.portal.security.auth0.AuthenticationService;
import com.poneres.portal.security.auth0.UserAuthorized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping
    @UserAuthorized(value = { "isAdmin", "isTenant"})
    public void save(@RequestBody Maintenance request) {
        String createdBy = authenticationService.getUserProfile().getId();

        request.setCreatedAt(LocalDateTime.now());
        request.setCreatedBy(createdBy);
        maintenanceRepository.save(request);
    }

    @GetMapping
    @UserAuthorized(value = { "isAdmin", "isTenant"})
    public Page<Maintenance> get(Pageable pageable) {
        return maintenanceRepository.findAll(pageable);
    }

}
