package com.example.ticketsystem.ticket;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class IssueTicketRequest {
    @NotBlank
    @Size(max = 100)
    private String passengerName;

    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }

}
