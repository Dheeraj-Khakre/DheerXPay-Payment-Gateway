package com.dheerx.DheerX_Pay.common.exceptions;

/**
 * Raised when a requested business resource cannot be located.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}

