package com.project.t1_openSchool.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String account_id = null;
        List<String> roles = new ArrayList<>();

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = getToken(authHeader);
            if (jwtUtils.isTokenValid(token)){
                log.info("JWT Token is valid");
                account_id = jwtUtils.getId(token);
                roles = jwtUtils.getRoles(token);
            } else {
                log.info("JWT token validation failed");
            }
        }

        Collection<? extends GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        if (account_id != null && !roles.isEmpty()) {
            UsernamePasswordAuthenticationToken springContextToken;
            springContextToken = new UsernamePasswordAuthenticationToken(account_id, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(springContextToken);
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(String authHeader) {
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
