package com.poneres.portal.agreements;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.poneres.portal.pdfs.processors.PdfType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;
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
    private String ssdId;
    private List<SignatureRecipient> recipients;
    private LocalDateTime createdAt;

    private Object getClient() {
        if (type.equals(PdfType.LEASE_AGREEMENT_MD_V1)) {
            return metadata.get("tenant");
        }

        if (type.equals(PdfType.LEAD_PAINT_ADDENDUM_MD_V1)) {
            return metadata.get("tenant");
        }

        return "-";
    }

    public String getFileName() {
        String fileName = createdAt.getYear() + "_" + type.getDisplayName() + "_" + getClient() + ".pdf";
        return fileName.replaceAll(" ", "_").replaceAll(",", "");
    }

    @JsonProperty
    boolean isSent() {
        return ssdId != null;
    }

}
