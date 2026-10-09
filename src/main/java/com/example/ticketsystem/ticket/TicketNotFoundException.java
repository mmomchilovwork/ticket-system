package com.example.ticketsystem.ticket;

import com.example.ticketsystem.common.ResourceNotFoundException;

public class TicketNotFoundException extends ResourceNotFoundException {

    public TicketNotFoundException(String code) {
        super("Ticket " + code + " not found");
    }
}
