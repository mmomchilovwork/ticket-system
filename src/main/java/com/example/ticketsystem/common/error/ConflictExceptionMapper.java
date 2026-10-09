package com.example.ticketsystem.common.error;

import com.example.ticketsystem.common.ConflictException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ConflictExceptionMapper implements ExceptionMapper<ConflictException> {
    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(ConflictException e) {
        return ProblemResponses.of(Response.Status.CONFLICT, e.getMessage(), uriInfo);
    }
}
