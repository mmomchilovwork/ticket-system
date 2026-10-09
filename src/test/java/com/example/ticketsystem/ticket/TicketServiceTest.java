package com.example.ticketsystem.ticket;

import com.example.ticketsystem.vehicle.Vehicle;
import com.example.ticketsystem.vehicle.VehicleService;
import com.example.ticketsystem.vehicle.VehicleType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.TypedQuery;
import java.time.Clock;
import java.time.Instant;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {
    private static final Instant NOW = Instant.parse("2026-10-09T10:00:00Z");

    @Mock
    private EntityManager em;

    @Mock
    private VehicleService vehicleService;

    @Mock
    private Clock clock;

    @Mock
    private TypedQuery<Ticket> query;

    @InjectMocks
    private TicketService ticketService;

    private final Vehicle vehicle = new Vehicle("CA1234AB", VehicleType.BUS, 50);

    @Test
    void issueUsesClockAndPersistsTicket() {
        when(vehicleService.findById(1L)).thenReturn(vehicle);
        when(clock.instant()).thenReturn(NOW);

        Ticket ticket = ticketService.issue(1L, "John Doe");

        assertEquals(NOW, ticket.getIssuedAt());
        verify(em).persist(ticket);
    }

    @Test
    void validateTranslatesOptimisticLockToConcurrentUpdate() {
        Ticket ticket = new Ticket(vehicle, "John Doe", NOW);
        stubFindByCode(ticket);
        when(clock.instant()).thenReturn(NOW);
        doThrow(new OptimisticLockException()).when(em).flush();

        assertThrows(ConcurrentTicketUpdateException.class,
                () -> ticketService.validate(ticket.getCode()));
    }

    @Test
    void findByCodeThrowsWhenTicketDoesNotExist() {
        when(em.createQuery(anyString(), eq(Ticket.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        assertThrows(TicketNotFoundException.class, () -> ticketService.findByCode("missing"));
    }

    private void stubFindByCode(Ticket ticket) {
        when(em.createQuery(anyString(), eq(Ticket.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.singletonList(ticket));
    }


}