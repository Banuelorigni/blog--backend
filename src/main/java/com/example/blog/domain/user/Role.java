package com.example.blog.domain.user;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class Role {
    private Long id;

    private String roleName;
}
