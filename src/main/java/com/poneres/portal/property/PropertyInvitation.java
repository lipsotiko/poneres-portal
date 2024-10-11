package com.poneres.portal.property;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyInvitation {
    @Id
    private String id;
    private String email;
    private String propertyId;
    private LocalDateTime createdAt;
}
