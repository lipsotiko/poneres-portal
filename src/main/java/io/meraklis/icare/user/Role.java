package io.meraklis.icare.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
public class Role {
    @Id
    private String id;
    private RoleType type;
    private String email;
}
