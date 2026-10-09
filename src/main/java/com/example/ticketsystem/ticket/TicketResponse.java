package com.example.ticketsystem.ticket;

import io.swagger.v3.oas.annotations.media.Schema;

public class TicketResponse {
    private String code;
    private String passengerName;
    @Schema(format = "date-time", example = "2026-10-08T20:30:00.123Z")
    private String issuedAt;
    private boolean validated;
    @Schema(format = "date-time", nullable = true)
    private String validatedAt;
    private Long vehicleId;
    private String vehicleRegistrationNumber;

    public static TicketResponse from(Ticket ticket) {
        TicketResponse r = new TicketResponse();
        r.code = ticket.getCode();
        r.passengerName = ticket.getPassengerName();
        r.issuedAt = ticket.getIssuedAt().toString();
        r.validated = ticket.isValidated();
        r.validatedAt = ticket.getValidatedAt() != null ? ticket.getValidatedAt().toString() : null;
        r.vehicleId = ticket.getVehicle().getId();
        r.vehicleRegistrationNumber = ticket.getVehicle().getRegistrationNumber();
        return r;
    }

    public String getCode() { return code; }
    public String getPassengerName() { return passengerName; }
    public String getIssuedAt() { return issuedAt; }
    public boolean isValidated() { return validated; }
    public String getValidatedAt() { return validatedAt; }
    public Long getVehicleId() { return vehicleId; }
    public String getVehicleRegistrationNumber() { return vehicleRegistrationNumber; }
}
