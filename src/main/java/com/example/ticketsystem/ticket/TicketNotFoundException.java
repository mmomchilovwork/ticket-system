package com.example.ticketsystem.ticket;

import com.example.ticketsystem.common.BusinessException;

public class TicketNotFoundException extends BusinessException {

    public TicketNotFoundException(String code) {
        super("Ticket " + code + " not found");
    }
}
