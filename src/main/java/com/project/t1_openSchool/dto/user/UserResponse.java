package com.project.t1_openSchool.dto.user;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UserResponse {

    private Long id;

    private String username;

    private String email;
}
