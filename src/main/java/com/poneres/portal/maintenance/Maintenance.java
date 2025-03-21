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
public class Maintenance {
    @Id
    private String id;
    private String type;
    private String priority;
    private String description;
    private String createdBy;
    private LocalDateTime createdAt;
}
