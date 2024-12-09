package com.project.t1_openSchool.dto.user;

import com.project.t1_openSchool.model.user.RoleType;
import lombok.Data;

@Data
public class UserRequest {

    private String username;

    private String email;

    private String password;

    private RoleType roleType;
}
