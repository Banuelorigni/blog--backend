package com.blog.domain.user;

import java.util.Arrays;

public enum RoleEnum {
    OPERATOR(1L, "operator"),
    PORTAL_USER(2L, "portal");

    private final Long roleType;

    private final String platformName;

    RoleEnum(Long roleType, String platformName) {
        this.roleType = roleType;
        this.platformName = platformName;
    }

    public Long getRoleType() {
        return roleType;
    }

    public String getByPlatformName() {
        return platformName;
    }

    public static RoleEnum getByPlatformName(String platformName) {
        return Arrays.stream(values())
                .filter(value -> value.getByPlatformName().equals(platformName))
                .findFirst()
                .orElseGet(null);
    }
}
