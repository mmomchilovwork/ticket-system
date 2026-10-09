package com.example.ticketsystem.common.error;

import com.example.ticketsystem.vehicle.VehicleResponse;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.core.Response;
import java.util.List;

@ApiResponse(responseCode = "201", description = "Vehicle registered",
        headers = @Header(name = "Location", description = "URI of the created vehicle",
                schema = @Schema(type = "string", format = "uri")),
        content = @Content(schema = @Schema(implementation = VehicleResponse.class)))
public class ValidationProblem extends Problem{

    private final List<Violation> violations;

    public ValidationProblem(String instance, List<Violation> violations) {
        super(Response.Status.BAD_REQUEST, "Request validation failed", instance);
        this.violations = violations;
    }

    public List<Violation> getViolations() { return violations; }

}
