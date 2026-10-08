package com.example.ticketsystem.ticket;

import com.example.ticketsystem.common.BusinessException;

public class ConcurrentTicketUpdateException extends BusinessException {


    protected ConcurrentTicketUpdateException(String code, Throwable cause) {
        super("Ticket " + code + " was modified concurrently", cause);
    }
}
