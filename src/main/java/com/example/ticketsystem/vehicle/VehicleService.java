package com.example.ticketsystem.vehicle;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class VehicleService {

    @PersistenceContext(unitName = "ticketSystemPU")
    private EntityManager em;

    public Vehicle register(String registrationNumber, VehicleType type, int capacity) {
        if (existsByRegistrationNumber(registrationNumber)) {
            throw new DuplicateRegistrationNumberException(registrationNumber);
        }
        Vehicle vehicle = new Vehicle(registrationNumber, type, capacity);
        em.persist(vehicle);
        return vehicle;
    }

    private boolean existsByRegistrationNumber(String registrationNumber) {
        Long count = em.createQuery(
                        "SELECT COUNT(v) FROM Vehicle v WHERE v.registrationNumber = :rn", Long.class)
                .setParameter("rn", registrationNumber)
                .getSingleResult();
        return count > 0;
    }

    public Vehicle findById(Long id) {
        Vehicle vehicle = em.find(Vehicle.class, id);
        if (vehicle == null) {
            throw new VehicleNotFoundException(id);
        }
        return vehicle;
    }
}
