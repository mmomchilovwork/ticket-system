package com.example.ticketsystem.vehicle;

import com.example.ticketsystem.common.ConflictException;

public class DuplicateRegistrationNumberException extends ConflictException {

    public DuplicateRegistrationNumberException(String registrationNumber) {
        super("Vehicle with registration number " + registrationNumber + " already exists");
    }
}
