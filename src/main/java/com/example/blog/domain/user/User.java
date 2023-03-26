package com.example.blog.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

import static com.example.blog.domain.user.RoleEnum.PORTAL_USER;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private Role role;
    private String username;
    private String password;
    private String nickname;

    private Instant createdAt;

    public void setPortalRole() {
        this.setRole(Role.builder().id(PORTAL_USER.getRoleType()).build());
    }
}
