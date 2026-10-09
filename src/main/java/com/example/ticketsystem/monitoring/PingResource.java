package com.example.ticketsystem.monitoring;

import io.swagger.v3.oas.annotations.Hidden;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/ping")
@RequestScoped
@Hidden
public class PingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String ping() {
        return "I'm alive";
    }
}
