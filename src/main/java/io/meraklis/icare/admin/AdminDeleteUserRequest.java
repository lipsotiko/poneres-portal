package io.meraklis.icare.admin;

import lombok.Data;

import java.util.List;

@Data
public class AdminDeleteUserRequest {
    private List<String> userIds;
}
