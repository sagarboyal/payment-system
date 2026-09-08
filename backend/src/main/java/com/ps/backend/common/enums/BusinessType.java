package com.ps.backend.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BusinessType {
    INDIVIDUAL("individual"),
    PROPRIETORSHIP("proprietorship"),
    PARTNERSHIP("partnership"),
    LLP("llp"),
    PRIVATE_LIMITED("private_limited"),
    PUBLIC_LIMITED("public_limited"),
    TRUST("trust"),
    SOCIETY("society"),
    NGO("ngo"),
    EDUCATIONAL_INSTITUTES("educational_institutes"),
    NOT_YET_REGISTERED("not_yet_registered");

    private final String value;

    BusinessType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static BusinessType fromValue(String text) {
        for (BusinessType b : BusinessType.values()) {
            if (b.value.equalsIgnoreCase(text) || b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Invalid business type: " + text);
    }
}
