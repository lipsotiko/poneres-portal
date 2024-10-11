package com.poneres.portal.security.auth0;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserVerificationStatus {
    private Boolean verified;
}
