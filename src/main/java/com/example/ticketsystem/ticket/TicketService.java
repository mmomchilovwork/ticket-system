package com.example.ticketsystem.ticket;

import com.example.ticketsystem.vehicle.Vehicle;
import com.example.ticketsystem.vehicle.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import java.time.Instant;
import java.util.List;

@Stateless
public class TicketService {

    private static final Logger LOG = LoggerFactory.getLogger(TicketService.class);

    @PersistenceContext(unitName = "ticketSystemPU")
    private EntityManager em;

    @Inject
    private VehicleService vehicleService;

    public Ticket issue(Long vehicleId, String passengerName) {
        Vehicle vehicle = vehicleService.findById(vehicleId);
        Ticket ticket = new Ticket(vehicle, passengerName, Instant.now());
        em.persist(ticket);
        LOG.info("Ticket issued: code={}, vehicleId={}", ticket.getCode(), vehicleId);
        return ticket;
    }

    public Ticket validate(String code) {
        Ticket ticket = findByCode(code);
        ticket.validate(Instant.now());
        try {
            em.flush();
        } catch (OptimisticLockException e) {
            throw new ConcurrentTicketUpdateException(code, e);
        }
        LOG.info("Ticket validated: code={}", code);
        return ticket;
    }

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

    public List<Ticket> listByVehicle(Long vehicleId) {
        vehicleService.findById(vehicleId);
        return em.createQuery(
                        "SELECT t FROM Ticket t WHERE t.vehicle.id = :vehicleId ORDER BY t.issuedAt DESC", Ticket.class)
                .setParameter("vehicleId", vehicleId)
                .getResultList();
    }

}
