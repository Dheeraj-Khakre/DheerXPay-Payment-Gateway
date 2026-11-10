package com.dheerx.DheerX_Pay.common.exceptions;

import lombok.Getter;

/**
 * Raised when a requested business resource cannot be located.
 */
@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final String resourceName;
    private final Object identifier;
    public ResourceNotFoundException(  String resourceName, Object identifier) {
        super(resourceName+" not found "+identifier);
        this.resourceName = resourceName;
        this.identifier = identifier;
    }
}

