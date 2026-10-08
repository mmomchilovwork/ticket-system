package com.example.ticketsystem.ticket;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/tickets")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
public class TicketResource {


    @Inject
    private TicketService ticketService;

    @GET
    @Path("/{code}")
    public TicketResponse get(@PathParam("code") String code) {
        return TicketResponse.from(ticketService.findByCode(code));
    }

    @POST
    @Path("/{code}/validation")
    public TicketResponse validate(@PathParam("code") String code) {
        return TicketResponse.from(ticketService.validate(code));
    }
}
