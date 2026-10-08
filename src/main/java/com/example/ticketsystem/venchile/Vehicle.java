package com.example.ticketsystem.venchile;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "vehicle",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_vehicle_registration_number",
                columnNames = "registration_number"))
public class Vehicle {

    @Id
    //TODO add generation strategy
    private Long id;

    @NotBlank
    @Size(max = 20)
    @Column(name = "registration_number", nullable = false, length = 20)
    private String registrationNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private VehicleType type;

    @Min(1)
    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    public Vehicle(String registrationNumber, VehicleType type, int capacity) {
        this.registrationNumber = registrationNumber;
        this.type = type;
        this.capacity = capacity;
    }

    public Vehicle() {
    }

    public Long getId() {
        return id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public VehicleType getType() {
        return type;
    }

    public int getCapacity() {
        return capacity;
    }

    //TODO override hashCode and equals to use registration number ?
}
