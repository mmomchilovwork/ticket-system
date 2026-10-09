package com.example.ticketsystem.common.error;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.UUID;
import java.util.logging.Level;

import com.example.ticketsystem.common.logging.RequestLoggingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;


@Provider
public class UnexpectedExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOG = LoggerFactory.getLogger(UnexpectedExceptionMapper.class);

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(Exception e) {
        LOG.error("Unexpected error", e);
        String reference = MDC.get(RequestLoggingFilter.REQUEST_ID_MDC_KEY);
        return ProblemResponses.of(Response.Status.INTERNAL_SERVER_ERROR,
                "Unexpected error. Reference: " + reference, uriInfo);
    }
}
