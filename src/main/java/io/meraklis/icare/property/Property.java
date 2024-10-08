package io.meraklis.icare.property;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Property {
    @Id
    private String id;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String entityId;
    private String createdBy;

    public String getFullAddress() {
        return String.format("%s, %s, %s %s", address, city, state, zipCode);
    }
}
