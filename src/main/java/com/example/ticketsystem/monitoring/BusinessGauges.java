package com.example.ticketsystem.monitoring;

import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Gauge;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class BusinessGauges {
    @Inject
    private MetricsService metricsService;

    @Gauge(name = "vehicles.registered", unit = MetricUnits.NONE, absolute = true,
            description = "Number of registered vehicles")
    public long vehicles() {
        return metricsService.countVehicles();
    }

    @Gauge(name = "tickets.issued", unit = MetricUnits.NONE, absolute = true,
            description = "Number of issued tickets")
    public long tickets() {
        return metricsService.countTickets();
    }

    @Gauge(name = "tickets.validated", unit = MetricUnits.NONE, absolute = true,
            description = "Number of validated tickets")
    public long validatedTickets() {
        return metricsService.countValidatedTickets();
    }
}
