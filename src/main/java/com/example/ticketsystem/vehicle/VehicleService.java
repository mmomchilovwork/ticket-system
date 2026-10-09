package com.example.ticketsystem.vehicle;

import com.example.ticketsystem.common.persistance.PersistenceErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

@Stateless
public class VehicleService {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleService.class);

    @PersistenceContext(unitName = "ticketSystemPU")
    private EntityManager em;

    public Vehicle register(String registrationNumber, VehicleType type, int capacity) {
        if (existsByRegistrationNumber(registrationNumber)) {
            throw new DuplicateRegistrationNumberException(registrationNumber);
        }
        Vehicle vehicle = new Vehicle(registrationNumber, type, capacity);
        em.persist(vehicle);
        try {
            em.flush();
        } catch (PersistenceException e) {
            if (PersistenceErrors.isUniqueViolation(e)) {
                throw new DuplicateRegistrationNumberException(registrationNumber);
            }
            throw e;
        }
        LOG.info("Vehicle registered: id={}, registrationNumber={}, type={}",
                vehicle.getId(), registrationNumber, type);
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
