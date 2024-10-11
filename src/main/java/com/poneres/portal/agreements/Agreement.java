package com.poneres.portal.agreements;

import com.poneres.portal.pdfs.processors.PdfType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Agreement {
    @Id
    private String id;
    private PdfType type;
    private Map<String, Object> metadata;
    private LocalDateTime createdAt;

    public Object getClient() {
        if (type.equals(PdfType.LEASE_AGREEMENT_MD_V1)) {
            return metadata.get("tenant");
        }

        return "-";
    }
}
