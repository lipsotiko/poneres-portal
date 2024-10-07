package io.meraklis.icare.entities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class Entity {
    @Id
    private String id;
    private String name;
    private String contactFirstName;
    private String contactLastName;
    private String contactEmail;
    private String contactAddress;
    private String contactCity;
    private String contactState;
    private String contactZipCode;
    private String createdBy;
}
