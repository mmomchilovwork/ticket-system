package com.example.ticketsystem.ticket;

import com.example.ticketsystem.common.ConflictException;

public class TicketAlreadyValidatedException extends ConflictException {

    public TicketAlreadyValidatedException(String code) {
        super("Ticket " + code + " is already validated");
    }
}
