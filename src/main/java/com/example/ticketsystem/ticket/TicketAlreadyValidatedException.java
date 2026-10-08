package com.example.ticketsystem.ticket;

import com.example.ticketsystem.common.BusinessException;

public class TicketAlreadyValidatedException extends BusinessException {

    public TicketAlreadyValidatedException(String code) {
        super("Ticket " + code + " is already validated");
    }
}
