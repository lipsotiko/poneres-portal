package com.poneres.portal.onboarding;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Onboarding {
    @Id
    private String id;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dob;
    private String specialty;
    private String location;
    private String resumeId;
    private String employmentType;
    private String createdBy;
    private LocalDateTime createdAt;
}
