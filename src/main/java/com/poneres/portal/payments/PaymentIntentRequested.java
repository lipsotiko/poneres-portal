package com.poneres.portal.payments;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Builder
public class PaymentIntentRequested {
    @Id
    private String id;
    private String paymentIntentId;
    private String email;
    private String status;
    private LocalDateTime createdAt;
}
