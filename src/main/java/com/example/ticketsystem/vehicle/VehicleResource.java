package com.example.ticketsystem.vehicle;

import io.swagger.v3.oas.annotations.Operation;
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

@Path("/vehicles")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Vehicle")
public class VehicleResource {

    @Inject
    private VehicleService vehicleService;

    @POST
    @Operation(operationId = "registerVehicle", summary = "Register a vehicle")
    @ApiResponse(responseCode = "201", description = "Vehicle registered",
            content = @Content(schema = @Schema(implementation = VehicleResponse.class)))
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
    @Operation(operationId = "getVehicle", summary = "Get a vehicle")
    @ApiResponse(responseCode = "200", description = "Vehicle found",
            content = @Content(schema = @Schema(implementation = VehicleResponse.class)))
    public VehicleResponse get(@PathParam("id") Long id) {
        return VehicleResponse.from(vehicleService.findById(id));
    }
}
