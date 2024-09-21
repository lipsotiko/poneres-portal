package io.meraklis.icare.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserVerificationStatus {
    private Boolean verified;
}
