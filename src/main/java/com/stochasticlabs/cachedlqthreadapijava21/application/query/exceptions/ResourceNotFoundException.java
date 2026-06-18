package com.stochasticlabs.cachedlqthreadapijava21.application.query.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}