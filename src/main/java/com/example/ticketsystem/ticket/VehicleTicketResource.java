package com.example.ticketsystem.ticket;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/vehicles/{vehicleId}/tickets")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Tickets")
public class VehicleTicketResource {

    @Inject
    private TicketService ticketService;

    @POST
    @Operation(operationId = "issueTicket", summary = "Issue a ticket for a vehicle")
    @ApiResponse(responseCode = "201", description = "Ticket issued",
            content = @Content(schema = @Schema(implementation = TicketResponse.class)))
    public Response issue(@PathParam("vehicleId") Long vehicleId,
                          @Valid @NotNull IssueTicketRequest request) {
        Ticket ticket = ticketService.issue(vehicleId, request.getPassengerName());
        // TODO K5: add Location header (UriInfo) pointing to /tickets/{code}
        return Response.status(Response.Status.CREATED)
                .entity(TicketResponse.from(ticket))
                .build();
    }

    @GET
    @Operation(operationId = "listVehicleTickets", summary = "List tickets of a vehicle")
    @ApiResponse(responseCode = "200", description = "Tickets of the vehicle, newest first",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TicketResponse.class))))
    public List<TicketResponse> list(@PathParam("vehicleId") Long vehicleId) {
        return ticketService.listByVehicle(vehicleId).stream()
                .map(TicketResponse::from)
                .collect(Collectors.toList());
    }
}
