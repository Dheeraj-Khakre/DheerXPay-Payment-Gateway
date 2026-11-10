package com.dheerx.DheerX_Pay.common.exceptions;

import lombok.Getter;

/**
 * Raised when the caller attempts to create a resource that already exists.
 */
@Getter
public class DuplicateResourceException extends RuntimeException {

    private String errorCode;
    public DuplicateResourceException(String errorCode, String message) {

        super(message);
        this.errorCode = errorCode;
    }
}

