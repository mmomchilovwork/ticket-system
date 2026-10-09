package com.example.ticketsystem.config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import java.time.Clock;

@ApplicationScoped
public class ClockProducer {

    @Produces
    public Clock clock() {
        return Clock.systemUTC();
    }
}
