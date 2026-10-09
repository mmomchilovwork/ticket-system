package com.example.ticketsystem.common.logging;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;



    @WebFilter("/api/*")
    public class RequestLoggingFilter implements Filter {

        public static final String REQUEST_ID_HEADER = "X-Request-Id";
        public static final String REQUEST_ID_MDC_KEY = "requestId";

        private static final Logger LOG = LoggerFactory.getLogger(RequestLoggingFilter.class);
        private static final Pattern VALID_REQUEST_ID = Pattern.compile("[A-Za-z0-9-]{1,64}");

        @Override
        public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
                throws IOException, ServletException {
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) res;

            String requestId = resolveRequestId(request.getHeader(REQUEST_ID_HEADER));
            long start = System.nanoTime();

            MDC.put(REQUEST_ID_MDC_KEY, requestId);
            response.setHeader(REQUEST_ID_HEADER, requestId);
            try {
                chain.doFilter(req, res);
            } finally {
                long durationMs = (System.nanoTime() - start) / 1_000_000;
                LOG.info("{} {} -> {} ({} ms)",
                        request.getMethod(), request.getRequestURI(), response.getStatus(), durationMs);
                MDC.remove(REQUEST_ID_MDC_KEY);
            }
        }

        private static String resolveRequestId(String incoming) {
            if (incoming != null && VALID_REQUEST_ID.matcher(incoming).matches()) {
                return incoming;
            }
            return UUID.randomUUID().toString();
        }
}
