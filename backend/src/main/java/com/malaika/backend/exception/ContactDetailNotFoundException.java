package com.malaika.backend.exception;

public class ContactDetailNotFoundException extends RuntimeException {

    public ContactDetailNotFoundException(String message) {
        super(message);
    }
}