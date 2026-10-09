package com.example.ticketsystem.ticket;

import com.example.ticketsystem.vehicle.Vehicle;
import com.example.ticketsystem.vehicle.VehicleType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TicketTest {
    private static final Instant ISSUED_AT = Instant.parse("2026-10-09T10:00:00Z");
    private static final Instant VALIDATED_AT = Instant.parse("2026-10-09T10:05:00Z");

    private final Vehicle vehicle = new Vehicle("CA1234AB", VehicleType.BUS, 50);

    @Test
    void newTicketIsNotValidated() {
        Ticket ticket = new Ticket(vehicle, "John Doe", ISSUED_AT);

        assertNotNull(ticket.getCode());
        assertEquals(ISSUED_AT, ticket.getIssuedAt());
        assertFalse(ticket.isValidated());
        assertNull(ticket.getValidatedAt());
    }

    @Test
    void validateSetsFlagAndTimestamp() {
        Ticket ticket = new Ticket(vehicle, "John Doe", ISSUED_AT);

        ticket.validate(VALIDATED_AT);

        assertTrue(ticket.isValidated());
        assertEquals(VALIDATED_AT, ticket.getValidatedAt());
    }

    @Test
    void secondValidationIsRejectedAndKeepsFirstTimestamp() {
        Ticket ticket = new Ticket(vehicle, "John Doe", ISSUED_AT);
        ticket.validate(VALIDATED_AT);

        assertThrows(TicketAlreadyValidatedException.class,
                () -> ticket.validate(VALIDATED_AT.plusSeconds(60)));
        assertEquals(VALIDATED_AT, ticket.getValidatedAt());
    }

    @Test
    void ticketsGetDifferentCodes() {
        Ticket first = new Ticket(vehicle, "A", ISSUED_AT);
        Ticket second = new Ticket(vehicle, "B", ISSUED_AT);

        assertNotEquals(first.getCode(), second.getCode());
    }

}