package com.project.t1_openSchool.services;

import com.project.t1_openSchool.dto.auth.AuthenticateRequest;
import com.project.t1_openSchool.dto.auth.AuthenticateResponse;
import com.project.t1_openSchool.dto.user.UserRequest;
import com.project.t1_openSchool.dto.user.UserResponse;
import com.project.t1_openSchool.model.user.Role;
import com.project.t1_openSchool.model.user.User;
import com.project.t1_openSchool.repository.UserRepository;
import com.project.t1_openSchool.security.AppUserPrincipal;
import com.project.t1_openSchool.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    public UserResponse createUser(UserRequest request) {
        User newUser = User.of(request);
        checkUserData(newUser);
        Role role = Role.from(request.getRoleType());
        newUser.setRoles(Collections.singletonList(role));
        role.setUser(newUser);
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(newUser);

        return UserResponse.builder()
                .id(newUser.getId())
                .username(newUser.getUsername())
                .email(newUser.getEmail())
                .build();
    }

    public AuthenticateResponse authenticateUser(AuthenticateRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtils.generateAccessToken((AppUserPrincipal) userDetails);

        return new AuthenticateResponse(token);
    }

    private void checkUserData(User newUser){
        if (userRepository.existsByUsername(newUser.getUsername())) {
            throw new RuntimeException(
                    MessageFormat.format("User with username {0} already exist.", newUser.getUsername()));
        } else if (userRepository.existsByEmail(newUser.getEmail())) {
            throw new RuntimeException(
                    MessageFormat.format("User with email {0} already exist.", newUser.getEmail()));
        }
    }
}
