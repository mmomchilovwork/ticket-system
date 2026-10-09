package com.example.ticketsystem.ticket;

import com.example.ticketsystem.common.Page;

import java.util.stream.Collectors;

public class TicketMapper {
    private TicketMapper() {
    }

    public static TicketResponse toResponse(Ticket ticket) {
        return new TicketResponse(
                ticket.getCode(),
                ticket.getPassengerName(),
                ticket.getIssuedAt().toString(),
                ticket.isValidated(),
                ticket.getValidatedAt() != null ? ticket.getValidatedAt().toString() : null,
                ticket.getVehicle().getId(),
                ticket.getVehicle().getRegistrationNumber());
    }

    public static TicketPageResponse toPageResponse(Page<Ticket> page) {
        return new TicketPageResponse(
                page.getItems().stream().map(TicketMapper::toResponse).collect(Collectors.toList()),
                page.getPage(), page.getSize(), page.getTotalItems());
    }
}
