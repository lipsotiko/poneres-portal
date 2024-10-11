package com.poneres.portal.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientDocument {
    @Id
    private String id;
    private String applicationId;
    private String fileName;
    private DocumentType type;
    private String uploadedBy;
}
