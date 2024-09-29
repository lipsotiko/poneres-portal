package io.meraklis.icare.payments;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
@Builder
@Document
public class PaymentAccountLink {
    private String url;
    private Long expiresAt;

    public LocalDateTime getExpiresAt() {
        return LocalDateTime.ofEpochSecond(expiresAt, 0, ZoneOffset.UTC);
    }
}
