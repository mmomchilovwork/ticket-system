package com.example.ticketsystem.ticket;

public class TicketAlreadyValidatedException extends RuntimeException{

    public TicketAlreadyValidatedException(String code) {
        super("Ticket " + code + " is already validated");
    }
}
