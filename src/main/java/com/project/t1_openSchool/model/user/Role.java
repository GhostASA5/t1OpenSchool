package com.project.t1_openSchool.model.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


@Entity
@Table(name = "roles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private RoleType role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public GrantedAuthority toGrantedAuthority() {
        return new SimpleGrantedAuthority(role.toString());
    }

    public static Role from(RoleType type) {
        var role = new Role();
        role.setRole(type);
        return role;
    }
}
