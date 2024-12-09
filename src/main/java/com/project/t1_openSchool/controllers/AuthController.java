package com.project.t1_openSchool.controllers;

import com.project.t1_openSchool.dto.auth.AuthenticateRequest;
import com.project.t1_openSchool.dto.auth.AuthenticateResponse;
import com.project.t1_openSchool.dto.user.UserRequest;
import com.project.t1_openSchool.dto.user.UserResponse;
import com.project.t1_openSchool.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.createUser(userRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticateResponse> login(@RequestBody AuthenticateRequest request) {
        return ResponseEntity.ok(userService.authenticateUser(request));
    }
}
