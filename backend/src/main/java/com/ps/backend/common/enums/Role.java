package com.ps.backend.common.enums;

public enum Role {
    ADMIN("admin"),
    MERCHANT("merchant"),
    USER("user");

    private final String value;

    Role(String value) {
        this.value = value;
    }
}

