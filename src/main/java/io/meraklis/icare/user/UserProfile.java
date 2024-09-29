package io.meraklis.icare.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    @Id
    private String id;

    @Indexed(name = "idx_user_profile_email", unique = true)
    private String email;
    private String firstName;
    private String lastName;
    private List<Role> roles;
    private String authProviderId;
    private String paymentProviderId;

    public UserProfile(String email, String firstName, String lastName, List<Role> roles) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }
}
