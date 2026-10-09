package com.example.ticketsystem.ticket;

import com.example.ticketsystem.common.error.Problem;
import com.example.ticketsystem.common.error.ProblemResponses;
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
    @Path("/{code}")
    @Operation(operationId = "getTicket", summary = "Get a ticket by its code", responses = {
            @ApiResponse(responseCode = "200", description = "Ticket found",
                    content = @Content(schema = @Schema(implementation = TicketResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ticket not found",
                    content = @Content(mediaType = ProblemResponses.PROBLEM_JSON,
                            schema = @Schema(implementation = Problem.class)))
    })
    public TicketResponse get(@PathParam("code") String code) {
        return TicketMapper.toResponse(ticketService.findByCode(code));
    }

    @POST
    @Path("/{code}/validation")
    @Operation(operationId = "validateTicket", summary = "Validate a ticket",
            description = "A ticket can be validated only once.", responses = {
            @ApiResponse(responseCode = "200", description = "Ticket validated",
                    content = @Content(schema = @Schema(implementation = TicketResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ticket not found",
                    content = @Content(mediaType = ProblemResponses.PROBLEM_JSON,
                            schema = @Schema(implementation = Problem.class))),
            @ApiResponse(responseCode = "409", description = "Ticket already validated or modified concurrently",
                    content = @Content(mediaType = ProblemResponses.PROBLEM_JSON,
                            schema = @Schema(implementation = Problem.class)))
    })
    public TicketResponse validate(@PathParam("code") String code) {
        return TicketMapper.toResponse(ticketService.validate(code));
    }
}
