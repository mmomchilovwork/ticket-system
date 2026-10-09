package com.example.ticketsystem.common.error;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(WebApplicationException e) {
        Response original = e.getResponse();
        Problem problem = new Problem(original.getStatusInfo(), e.getMessage(), ProblemResponses.path(uriInfo));
        return ProblemResponses.build(Response.fromResponse(original), problem);
    }
}
