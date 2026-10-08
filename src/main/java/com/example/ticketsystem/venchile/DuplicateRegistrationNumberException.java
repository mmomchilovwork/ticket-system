package com.example.ticketsystem.venchile;

import com.example.ticketsystem.common.BusinessException;

public class DuplicateRegistrationNumberException extends BusinessException {

    public DuplicateRegistrationNumberException(String registrationNumber) {
        super("Vehicle with registration number " + registrationNumber + " already exists");
    }
}
