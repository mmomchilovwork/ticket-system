package com.example.ticketsystem.ticket;

import com.example.ticketsystem.common.ConflictException;

public class ConcurrentTicketUpdateException extends ConflictException {


    protected ConcurrentTicketUpdateException(String code, Throwable cause) {
        super("Ticket " + code + " was modified concurrently", cause);
    }
}
