package com.jonnymatts.prometheus.demo;

import io.prometheus.client.Counter;
import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.connector.ResponseFacade;

import javax.servlet.*;
import java.io.IOException;

public class HttpRequestMetricFilter implements Filter {

    private final Counter counter;

    public HttpRequestMetricFilter(Counter counter) {
        this.counter = counter;
    }

    public HttpRequestMetricFilter() {
        this.counter = Counter.build()
                .name("http_request_total")
                .help("Total HTTP requests handled")
                .labelNames("method", "route", "status_code")
                .create()
                .register();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final RequestFacade requestFacade = (RequestFacade) request;
        final ResponseFacade responseFacade = (ResponseFacade) response;

        chain.doFilter(request, response);

        counter.labels(requestFacade.getMethod(), requestFacade.getServletPath(), String.valueOf(responseFacade.getStatus())).inc();
    }

    @Override
    public void destroy() {}
}
