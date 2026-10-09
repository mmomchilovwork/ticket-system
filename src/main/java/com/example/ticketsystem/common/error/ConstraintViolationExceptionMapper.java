package com.example.ticketsystem.common.error;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ElementKind;
import javax.validation.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(ConstraintViolationException e) {
        List<Violation> violations = e.getConstraintViolations().stream()
                .map(v -> new Violation(fieldName(v), v.getMessage()))
                .sorted(Comparator.comparing(Violation::getField))
                .collect(Collectors.toList());
        return ProblemResponses.build(
                Response.status(Response.Status.BAD_REQUEST),
                new ValidationProblem(ProblemResponses.path(uriInfo), violations));
    }

    private static String fieldName(ConstraintViolation<?> violation) {
        String parameterName = null;
        StringJoiner property = new StringJoiner(".");
        for (Path.Node node : violation.getPropertyPath()) {
            if (node.getKind() == ElementKind.METHOD) {
                continue;
            }
            if (node.getKind() == ElementKind.PARAMETER) {
                parameterName = node.getName();
                continue;
            }
            property.add(node.getName());
        }
        return property.length() > 0 ? property.toString() : parameterName;
    }
}
