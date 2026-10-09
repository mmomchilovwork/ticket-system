package com.example.ticketsystem.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
@OpenAPIDefinition(
        info = @Info(
                title = "Ticket System API",
                version = "1.0.0",
                description = "Vehicle registration, ticket issuing and validation for public transport"),
        servers = @Server(url = "/ticket-system/api"))
public class JaxRsConfig extends Application {
}
