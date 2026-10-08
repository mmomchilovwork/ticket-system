package com.example.ticketsystem.ticket;

public class TicketResponse {
    private String code;
    private String passengerName;
    private String issuedAt;
    private boolean validated;
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
