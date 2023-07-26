package com.kursovaya.exception.domain;

public class PasswordNotValidException extends Exception{
    public PasswordNotValidException(String message) {
        super(message);
    }
}
