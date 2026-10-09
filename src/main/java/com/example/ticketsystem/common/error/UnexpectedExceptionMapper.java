package com.example.ticketsystem.common.error;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class UnexpectedExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(UnexpectedExceptionMapper.class.getName());

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(Exception e) {
        String errorId = UUID.randomUUID().toString();
        LOG.log(Level.SEVERE, "Unexpected error [errorId=" + errorId + "]", e);
        return ProblemResponses.of(Response.Status.INTERNAL_SERVER_ERROR,
                "Unexpected error. Reference: " + errorId, uriInfo);
    }
}
