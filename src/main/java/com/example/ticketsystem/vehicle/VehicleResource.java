package com.example.ticketsystem.vehicle;

import com.example.ticketsystem.common.error.Problem;
import com.example.ticketsystem.common.error.ProblemResponses;
import com.example.ticketsystem.common.error.ValidationProblem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("/vehicles")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Vehicle")
public class VehicleResource {

    @Inject
    private VehicleService vehicleService;

    @Context
    private UriInfo uriInfo;

    @POST
    @Operation(operationId = "registerVehicle", summary = "Register a vehicle", responses = {
            @ApiResponse(responseCode = "201", description = "Vehicle registered",
                    headers = @Header(name = "Location", description = "URI of the created vehicle",
                            schema = @Schema(type = "string", format = "uri")),
                    content = @Content(schema = @Schema(implementation = VehicleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(mediaType = ProblemResponses.PROBLEM_JSON,
                            schema = @Schema(implementation = ValidationProblem.class))),
            @ApiResponse(responseCode = "409", description = "Registration number already exists",
                    content = @Content(mediaType = ProblemResponses.PROBLEM_JSON,
                            schema = @Schema(implementation = Problem.class)))
    })
    public Response register(@Valid @NotNull CreateVehicleRequest request) {
        Vehicle vehicle = vehicleService.register(
                request.getRegistrationNumber(), request.getType(), request.getCapacity());
        URI location = uriInfo.getBaseUriBuilder()
                .path(VehicleResource.class)
                .path(String.valueOf(vehicle.getId()))
                .build();
        return Response.created(location).entity(VehicleMapper.toResponse(vehicle)).build();
    }

    @GET
    @Path("/{id}")
    @Operation(operationId = "getVehicle", summary = "Get a vehicle", responses = {
            @ApiResponse(responseCode = "200", description = "Vehicle found",
                    content = @Content(schema = @Schema(implementation = VehicleResponse.class))),
            @ApiResponse(responseCode = "404", description = "Vehicle not found",
                    content = @Content(mediaType = ProblemResponses.PROBLEM_JSON,
                            schema = @Schema(implementation = Problem.class)))
    })
    public VehicleResponse get(@PathParam("id") Long id) {
        return VehicleMapper.toResponse(vehicleService.findById(id));
    }
}
