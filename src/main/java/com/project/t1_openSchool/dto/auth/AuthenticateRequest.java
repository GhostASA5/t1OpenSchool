package com.project.t1_openSchool.dto.auth;

import lombok.Data;

@Data
public class AuthenticateRequest {

    private String email;

    private String password;
}
