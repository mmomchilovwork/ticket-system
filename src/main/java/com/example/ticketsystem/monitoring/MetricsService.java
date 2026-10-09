package com.example.ticketsystem.monitoring;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class MetricsService {
    @PersistenceContext(unitName = "ticketSystemPU")
    private EntityManager em;

    public long countVehicles() {
        return count("SELECT COUNT(v) FROM Vehicle v");
    }

    public long countTickets() {
        return count("SELECT COUNT(t) FROM Ticket t");
    }

    public long countValidatedTickets() {
        return count("SELECT COUNT(t) FROM Ticket t WHERE t.validated = true");
    }

    private long count(String jpql) {
        return em.createQuery(jpql, Long.class).getSingleResult();
    }
}
