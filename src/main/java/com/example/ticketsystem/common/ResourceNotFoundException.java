package com.example.ticketsystem.common;

public class ResourceNotFoundException extends BusinessException{

    protected ResourceNotFoundException(String message) {
        super(message);
    }
}
