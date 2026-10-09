package com.example.ticketsystem.common.error;

import javax.ws.rs.core.Response;
import java.util.List;

public class ValidationProblem extends Problem{

    private final List<Violation> violations;

    public ValidationProblem(String instance, List<Violation> violations) {
        super(Response.Status.BAD_REQUEST, "Request validation failed", instance);
        this.violations = violations;
    }

    public List<Violation> getViolations() { return violations; }

}
