package com.example.ticketsystem.vehicle;

public class VehicleResponse {
    private final Long id;
    private final String registrationNumber;
    private final VehicleType type;
    private final int capacity;

    public VehicleResponse(Long id, String registrationNumber, VehicleType type, int capacity) {
        this.id = id;
        this.registrationNumber = registrationNumber;
        this.type = type;
        this.capacity = capacity;
    }

    public Long getId() { return id; }
    public String getRegistrationNumber() { return registrationNumber; }
    public VehicleType getType() { return type; }
    public int getCapacity() { return capacity; }
}
