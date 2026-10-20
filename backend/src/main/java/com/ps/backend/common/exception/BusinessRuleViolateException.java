package com.ps.backend.common.exception;

import lombok.Getter;

@Getter
public class BusinessRuleViolateException extends  RuntimeException {

    private final String errorCode;

    public BusinessRuleViolateException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
