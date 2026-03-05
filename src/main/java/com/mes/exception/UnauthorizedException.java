package com.mes.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException() {
        super("未授权访问");
    }
}
