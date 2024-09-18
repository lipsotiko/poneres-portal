package io.meraklis.icare.top_flight.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import static io.meraklis.icare.top_flight.user.UserHelpers.isInstructor;

@Tag(name = "Top Flight Scheduler")
@RestController
@RequestMapping("/api/top-flight/user")
public class TopFlightUserController {

    @Operation(summary = "Update pilot information")
    @PostMapping("/{id}")
    @Transactional
    public void updatePilot(
            @RequestParam Integer pilotId,
            @RequestBody TopFlightUserUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (!isInstructor(userDetails.getUsername())) {
            return;
        }

        // TODO: Update pilot information
    }
}
