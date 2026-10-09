package com.example.ticketsystem.monitoring;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseHealthCheck implements HealthCheck {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseHealthCheck.class);
    private static final int VALIDATION_TIMEOUT_SECONDS = 2;

    @Resource(lookup = "java:jboss/datasources/TicketSystemDS")
    private DataSource dataSource;

    @Override
    public HealthCheckResponse call() {
        try (Connection connection = dataSource.getConnection()) {
            boolean valid = connection.isValid(VALIDATION_TIMEOUT_SECONDS);
            return HealthCheckResponse.named("database").state(valid).build();
        } catch (SQLException e) {
            LOG.warn("Database health check failed: {}", e.getMessage());
            return HealthCheckResponse.named("database").down().build();
        }
    }
}
