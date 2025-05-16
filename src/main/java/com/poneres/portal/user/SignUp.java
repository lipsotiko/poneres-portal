package com.poneres.portal.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUp {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private List<Role> roles;
    private String phoneNumber;
}
