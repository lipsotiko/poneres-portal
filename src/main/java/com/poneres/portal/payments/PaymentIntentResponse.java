package com.poneres.portal.payments;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentIntentResponse {
    public String clientSecret;
}
