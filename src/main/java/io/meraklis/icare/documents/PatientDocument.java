package io.meraklis.icare.documents;

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
    private String uploadedBy;
}
