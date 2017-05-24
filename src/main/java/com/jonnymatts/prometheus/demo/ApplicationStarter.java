package com.jonnymatts.prometheus.demo;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.hotspot.DefaultExports;
import io.prometheus.client.spring.boot.EnablePrometheusEndpoint;
import io.prometheus.client.spring.boot.EnableSpringBootMetricsCollector;
import io.prometheus.client.spring.web.EnablePrometheusTiming;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;

@EnableSpringBootMetricsCollector
@EnablePrometheusEndpoint
@EnablePrometheusTiming
@SpringBootApplication
public class ApplicationStarter {

    public static final String FAILED_QUEUED_REQUESTS_METRIC_NAME = "failed_queued_requests";
    public static final String DATABASE_CONNECTION_METRIC_NAME = "database_connection";
    public static final String APPLICATION_VERSION_METRIC_NAME = "application_version";

    @PostConstruct
    public void setUp() {
        domainController().connectDatabase();
        skyAppMetricGauge().labels(APPLICATION_VERSION_METRIC_NAME, "").set(200);
    }

    public static void main(String[] args) throws Exception {
        DefaultExports.initialize();
        SpringApplication.run(ApplicationStarter.class, args);
    }

    @Bean
    public Gauge skyAppMetricGauge() {
        return Gauge.build()
                .name("sky_app_metric")
                .help("Generic Sky app metric.")
                .labelNames("metric_name", "data")
                .create()
                .register();
    }

    @Bean
    public DomainController domainController() {
        return new DomainController(
                "jdbc://random-database-url",
                skyAppMetricGauge()
                        .labels(FAILED_QUEUED_REQUESTS_METRIC_NAME, ""),
                skyAppMetricGauge()
                        .labels(DATABASE_CONNECTION_METRIC_NAME, "databaseUrl=jdbc://random-database-url")
        );
    }

    @Bean
    public FilterRegistrationBean httpMetricFilter() {
        return new FilterRegistrationBean(new HttpRequestMetricFilter());
    }
}