package com.dheerx.DheerX_Pay.common.exceptions;

/**
 * Raised when the caller attempts to create a resource that already exists.
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}

