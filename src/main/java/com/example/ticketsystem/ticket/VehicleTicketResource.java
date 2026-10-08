package com.example.ticketsystem.ticket;

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
public class VehicleTicketResource {

    @Inject
    private TicketService ticketService;

    @POST
    public Response issue(@PathParam("vehicleId") Long vehicleId,
                          @Valid @NotNull IssueTicketRequest request) {
        Ticket ticket = ticketService.issue(vehicleId, request.getPassengerName());
        // TODO K5: add Location header (UriInfo) pointing to /tickets/{code}
        return Response.status(Response.Status.CREATED)
                .entity(TicketResponse.from(ticket))
                .build();
    }

    @GET
    public List<TicketResponse> list(@PathParam("vehicleId") Long vehicleId) {
        return ticketService.listByVehicle(vehicleId).stream()
                .map(TicketResponse::from)
                .collect(Collectors.toList());
    }
}
