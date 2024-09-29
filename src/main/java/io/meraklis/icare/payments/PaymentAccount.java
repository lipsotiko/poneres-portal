package io.meraklis.icare.payments;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class PaymentAccount {
    private Set<String> requirementsDue;
    private Boolean pendingVerification;
}
