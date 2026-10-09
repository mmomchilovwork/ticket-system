package com.example.ticketsystem.ticket;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/tickets")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Tickets")
public class TicketResource {


    @Inject
    private TicketService ticketService;

    @GET
    @Path("/{code}")@Operation(operationId = "getTicket", summary = "Get a ticket by its code")
    @ApiResponse(responseCode = "200", description = "Ticket found",
            content = @Content(schema = @Schema(implementation = TicketResponse.class)))
    public TicketResponse get(@PathParam("code") String code) {
        return TicketResponse.from(ticketService.findByCode(code));
    }

    @POST
    @Path("/{code}/validation")
    @Operation(summary = "Validate a ticket",
            description = "A ticket can be validated only once.")
    @ApiResponse(responseCode = "200", description = "Ticket validated",
            content = @Content(schema = @Schema(implementation = TicketResponse.class)))
    public TicketResponse validate(@PathParam("code") String code) {
        return TicketResponse.from(ticketService.validate(code));
    }
}
