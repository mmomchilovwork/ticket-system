package com.example.ticketsystem.venchile;

import com.example.ticketsystem.common.ResourceNotFoundException;

public class VehicleNotFoundException extends ResourceNotFoundException {

    public VehicleNotFoundException(Long id) {
        super("Vehicle " + id + " not found");
    }
}
