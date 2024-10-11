package com.poneres.portal.signatures;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Signature {
    @Id
    private String id;
    private String base64;
    private SignatureType type;
    private String uploadedBy;
    private LocalDateTime uploadedAt;
}
