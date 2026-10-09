package com.example.ticketsystem.vehicle;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/vehicles")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VehicleResource {

    @Inject
    private VehicleService vehicleService;

    @POST
    public Response register(@Valid @NotNull CreateVehicleRequest request) {
        Vehicle vehicle = vehicleService.register(
                request.getRegistrationNumber(), request.getType(), request.getCapacity());
        // TODO K5: add Location header (UriInfo)
        return Response.status(Response.Status.CREATED)
                .entity(VehicleResponse.from(vehicle))
                .build();
    }

    @GET
    @Path("/{id}")
    public VehicleResponse get(@PathParam("id") Long id) {
        return VehicleResponse.from(vehicleService.findById(id));
    }
}
