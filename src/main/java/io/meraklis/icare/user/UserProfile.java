package io.meraklis.icare.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private Role type;
    private String authProviderId;

    public UserProfile(Role type, String email, String firstName, String lastName) {
        this.type = type;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
