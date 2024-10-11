package com.poneres.portal.admin;

import lombok.Data;

import java.util.List;

@Data
public class AdminDeleteUserRequest {
    private List<String> userIds;
}
