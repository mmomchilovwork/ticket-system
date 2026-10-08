package com.example.ticketsystem.venchile;

public class VehicleResponse {
    private Long id;
    private String registrationNumber;
    private VehicleType type;
    private int capacity;

    public static VehicleResponse from(Vehicle vehicle) {
        VehicleResponse r = new VehicleResponse();
        r.id = vehicle.getId();
        r.registrationNumber = vehicle.getRegistrationNumber();
        r.type = vehicle.getType();
        r.capacity = vehicle.getCapacity();
        return r;
    }

    public Long getId() { return id; }
    public String getRegistrationNumber() { return registrationNumber; }
    public VehicleType getType() { return type; }
    public int getCapacity() { return capacity; }
}
