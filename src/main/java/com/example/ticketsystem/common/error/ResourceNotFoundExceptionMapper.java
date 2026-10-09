package com.example.ticketsystem.common.error;

import com.example.ticketsystem.common.ResourceNotFoundException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ResourceNotFoundExceptionMapper implements ExceptionMapper<ResourceNotFoundException> {
    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(ResourceNotFoundException e) {
        return ProblemResponses.of(Response.Status.NOT_FOUND, e.getMessage(), uriInfo);
    }

    ;
}