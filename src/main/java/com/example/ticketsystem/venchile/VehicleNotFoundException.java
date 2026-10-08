package com.example.ticketsystem.venchile;

import com.example.ticketsystem.common.BusinessException;

public class VehicleNotFoundException extends BusinessException {
    public VehicleNotFoundException(Long id) {
        super("Vehicle " + id + " not found");
    }
}
