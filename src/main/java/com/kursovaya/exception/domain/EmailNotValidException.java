package com.kursovaya.exception.domain;

public class EmailNotValidException extends Exception {
    public EmailNotValidException(String message) {
        super(message);
    }
}
