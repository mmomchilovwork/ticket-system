package com.example.ticketsystem.common.error;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public class ProblemResponses {
    public static final String PROBLEM_JSON = "application/problem+json";

    private ProblemResponses() {
    }

    public static Response of(Response.StatusType status, String detail, UriInfo uriInfo) {
        return build(Response.status(status), new Problem(status, detail, path(uriInfo)));
    }

    static Response build(Response.ResponseBuilder builder, Problem problem) {
        return builder.type(PROBLEM_JSON).entity(problem).build();
    }

    static String path(UriInfo uriInfo) {
        return "/" + uriInfo.getPath();
    }
}
