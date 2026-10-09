package com.example.ticketsystem.ticket;

import com.example.ticketsystem.common.Page;
import com.example.ticketsystem.vehicle.Vehicle;
import com.example.ticketsystem.vehicle.VehicleService;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import java.time.Clock;
import java.time.Instant;
import java.util.List;

@Stateless
public class TicketService {

    private static final Logger LOG = LoggerFactory.getLogger(TicketService.class);

    @PersistenceContext(unitName = "ticketSystemPU")
    private EntityManager em;

    @Inject
    private VehicleService vehicleService;

    @Inject
    private Clock clock;

    @Timed(name = "tickets.issue", absolute = true, description = "Ticket issuing")
    public Ticket issue(Long vehicleId, String passengerName) {
        Vehicle vehicle = vehicleService.findById(vehicleId);
        Ticket ticket = new Ticket(vehicle, passengerName, Instant.now(clock));
        em.persist(ticket);
        LOG.info("Ticket issued: code={}, vehicleId={}", ticket.getCode(), vehicleId);
        return ticket;
    }

    @Timed(name = "tickets.validate", absolute = true, description = "Ticket validation")
    public Ticket validate(String code) {
        Ticket ticket = findByCode(code);
        ticket.validate(Instant.now(clock));
        try {
            em.flush();
        } catch (OptimisticLockException e) {
            throw new ConcurrentTicketUpdateException(code, e);
        }
        LOG.info("Ticket validated: code={}", code);
        return ticket;
    }

    @Timed(name = "tickets.find", absolute = true, description = "Ticket lookup by code")
    public Ticket findByCode(String code) {
        List<Ticket> result = em.createQuery(
                        "SELECT t FROM Ticket t JOIN FETCH t.vehicle WHERE t.code = :code", Ticket.class)
                .setParameter("code", code)
                .getResultList();
        if (result.isEmpty()) {
            throw new TicketNotFoundException(code);
        }
        return result.get(0);
    }

    @Timed(name = "tickets.list", absolute = true, description = "Ticket listing per vehicle")
    public Page<Ticket> listByVehicle(Long vehicleId, int page, int size) {
        vehicleService.findById(vehicleId);
        List<Ticket> items = em.createQuery(
                        "SELECT t FROM Ticket t WHERE t.vehicle.id = :vehicleId "
                                + "ORDER BY t.issuedAt DESC, t.id DESC", Ticket.class)
                .setParameter("vehicleId", vehicleId)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
        Long total = em.createQuery(
                        "SELECT COUNT(t) FROM Ticket t WHERE t.vehicle.id = :vehicleId", Long.class)
                .setParameter("vehicleId", vehicleId)
                .getSingleResult();
        return new Page<>(items, page, size, total);
    }

}
