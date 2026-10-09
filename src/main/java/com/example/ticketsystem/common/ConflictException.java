package com.example.ticketsystem.common;

public abstract class ConflictException extends BusinessException{

    protected ConflictException(String message) {
        super(message);
    }

    protected ConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
