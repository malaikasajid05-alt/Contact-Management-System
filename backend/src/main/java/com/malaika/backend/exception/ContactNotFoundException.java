package com.malaika.backend.exception;

public class ContactNotFoundException extends RuntimeException {

    public ContactNotFoundException(String message) {

        super(message);
    }
}