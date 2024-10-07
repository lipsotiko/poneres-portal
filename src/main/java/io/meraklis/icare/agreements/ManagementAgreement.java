package io.meraklis.icare.agreements;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Data
@Builder
public class ManagementAgreement {
    @Id
    private String id;
    private String entityId;
    private LocalDate startDate;
    private LocalDate endDate;
}
