package com.endrezadravecz.rest.exception;

public class EntityNotFoundException extends Exception {

    public EntityNotFoundException(String errorMessage) {
        super(errorMessage);
    }

}