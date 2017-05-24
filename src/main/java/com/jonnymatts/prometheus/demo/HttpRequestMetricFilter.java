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
                .name("http_requests_total")
                .help("Total HTTP requests handled")
                .labelNames("method", "handler", "code")
                .create()
                .register();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);

        final RequestFacade requestFacade = (RequestFacade) request;
        final ResponseFacade responseFacade = (ResponseFacade) response;

        counter.labels(
                requestFacade.getMethod().toLowerCase(),
                requestFacade.getServletPath().substring(1),
                String.valueOf(responseFacade.getStatus())
        ).inc();
    }

    @Override
    public void destroy() {}
}
