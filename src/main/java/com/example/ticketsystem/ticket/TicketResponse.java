package com.example.ticketsystem.ticket;

import io.swagger.v3.oas.annotations.media.Schema;

public class TicketResponse {
    private final String code;
    private final String passengerName;
    @Schema(format = "date-time", example = "2026-10-08T20:30:00.123Z")
    private final String issuedAt;
    private final boolean validated;
    @Schema(format = "date-time", nullable = true)
    private final String validatedAt;
    private final Long vehicleId;
    private final String vehicleRegistrationNumber;

    public TicketResponse(String code,
                          String passengerName,
                          String issuedAt,
                          boolean validated,
                          String validatedAt,
                          Long vehicleId,
                          String vehicleRegistrationNumber) {
        this.code = code;
        this.passengerName = passengerName;
        this.issuedAt = issuedAt;
        this.validated = validated;
        this.validatedAt = validatedAt;
        this.vehicleId = vehicleId;
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
    }


    public String getCode() {
        return code;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public String getIssuedAt() {
        return issuedAt;
    }

    public boolean isValidated() {
        return validated;
    }

    public String getValidatedAt() {
        return validatedAt;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public String getVehicleRegistrationNumber() {
        return vehicleRegistrationNumber;
    }
}
