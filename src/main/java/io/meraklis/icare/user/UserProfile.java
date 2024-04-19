package io.meraklis.icare.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
public class UserProfile {
    @Id
    private String id;
    private Role type;
    private String email;
    private String firstName;
    private String lastName;

    public UserProfile(Role type, String email, String firstName, String lastName) {
        this.type = type;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
