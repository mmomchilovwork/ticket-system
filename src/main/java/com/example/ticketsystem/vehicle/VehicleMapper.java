package com.example.ticketsystem.vehicle;

public class VehicleMapper {
    private VehicleMapper() {
    }

    public static VehicleResponse toResponse(Vehicle vehicle) {
        return new VehicleResponse(vehicle.getId(), vehicle.getRegistrationNumber(),
                vehicle.getType(), vehicle.getCapacity());
    }
}
