package com.example.ticketsystem.common.error;

import javax.ws.rs.core.Response;
import java.time.Instant;

public class Problem {
    private final String title;
    private final int status;
    private final String detail;
    private final String instance;
    private final String timestamp;

    public Problem(Response.StatusType status, String detail, String instance) {
        this.title = status.getReasonPhrase();
        this.status = status.getStatusCode();
        this.detail = detail;
        this.instance = instance;
        this.timestamp = Instant.now().toString();
    }

    public String getTitle() { return title; }
    public int getStatus() { return status; }
    public String getDetail() { return detail; }
    public String getInstance() { return instance; }
    public String getTimestamp() { return timestamp; }
}
