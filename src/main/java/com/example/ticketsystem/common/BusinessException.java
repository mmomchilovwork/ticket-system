package com.example.ticketsystem.common;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true, inherited = true)
public class BusinessException extends RuntimeException{

    protected BusinessException(String message) {
        super(message);
    }

    protected BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
