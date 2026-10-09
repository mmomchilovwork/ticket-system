package com.example.ticketsystem.ticket;

import com.example.ticketsystem.common.error.Problem;
import com.example.ticketsystem.common.error.ProblemResponses;
import com.example.ticketsystem.common.error.ValidationProblem;
import com.example.ticketsystem.vehicle.Vehicle;
import com.example.ticketsystem.vehicle.VehicleResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
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

    @Context
    private UriInfo uriInfo;

    @POST
    @Operation(operationId = "issueTicket", summary = "Issue a ticket for a vehicle", responses = {
            @ApiResponse(responseCode = "201", description = "Ticket issued",
                    headers = @Header(name = "Location", description = "URI of the created ticket",
                            schema = @Schema(type = "string", format = "uri")),
                    content = @Content(schema = @Schema(implementation = TicketResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(mediaType = ProblemResponses.PROBLEM_JSON,
                            schema = @Schema(implementation = ValidationProblem.class))),
            @ApiResponse(responseCode = "404", description = "Vehicle not found",
                    content = @Content(mediaType = ProblemResponses.PROBLEM_JSON,
                            schema = @Schema(implementation = Problem.class)))
    })
    public Response issue(@PathParam("vehicleId") Long vehicleId,
                          @Valid @NotNull IssueTicketRequest request) {
        Ticket ticket = ticketService.issue(vehicleId, request.getPassengerName());

        URI location = uriInfo.getBaseUriBuilder()
                .path(TicketResource.class)
                .path(ticket.getCode())
                .build();

        return Response.created(location)
                .entity(TicketMapper.toResponse(ticket))
                .build();
    }

    @GET
    @Operation(operationId = "listVehicleTickets", summary = "List tickets of a vehicle, newest first", responses = {
            @ApiResponse(responseCode = "200", description = "Page of tickets",
                    content = @Content(schema = @Schema(implementation = TicketPageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid paging parameters",
                    content = @Content(mediaType = ProblemResponses.PROBLEM_JSON,
                            schema = @Schema(implementation = ValidationProblem.class))),
            @ApiResponse(responseCode = "404", description = "Vehicle not found",
                    content = @Content(mediaType = ProblemResponses.PROBLEM_JSON,
                            schema = @Schema(implementation = Problem.class)))
    })
    public TicketPageResponse list(
            @PathParam("vehicleId") Long vehicleId,
            @QueryParam("page") @DefaultValue("0") @Min(0) @Max(10_000) int page,
            @QueryParam("size") @DefaultValue("20") @Min(1) @Max(100) int size) {
        return TicketMapper.toPageResponse(ticketService.listByVehicle(vehicleId, page, size));
    }
}
