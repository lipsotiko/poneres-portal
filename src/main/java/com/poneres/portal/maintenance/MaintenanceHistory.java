package com.poneres.portal.maintenance;

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
public class MaintenanceHistory {
    @Id
    private String id;
    private MaintenanceStatus fromStatus;
    private MaintenanceStatus toStatus;
    private LocalDateTime createdAt;
    private String updatedBy;
}
