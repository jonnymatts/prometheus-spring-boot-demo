package com.jonnymatts.prometheus.demo;

import io.prometheus.client.Gauge;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.String.format;

@RestController
public class DomainController {

    private final String databaseUrl;
    private final Gauge.Child failedRequests;
    private final Gauge.Child databaseConnection;

    public DomainController(String databaseUrl,
                            Gauge.Child failedRequests,
                            Gauge.Child databaseConnection) {
        this.databaseUrl = databaseUrl;
        this.failedRequests = failedRequests;
        this.databaseConnection = databaseConnection;
    }

    @RequestMapping(path = "failed/inc")
    public ResponseEntity<String> increaseFailedRequests() {
        failedRequests.inc();
        return ResponseEntity.ok(String.valueOf(failedRequests.get()));
    }

    @RequestMapping(path = "failed/dec")
    public ResponseEntity<String> decreaseFailedRequests() {
        failedRequests.dec();
        return ResponseEntity.ok(String.valueOf(failedRequests.get()));
    }

    @RequestMapping(path = "database/connect")
    public ResponseEntity<String> connectDatabase() {
        databaseConnection
                .set(1);
        return ResponseEntity.ok(String.valueOf(format("Database %s connected", databaseUrl)));
    }

    @RequestMapping(path = "database/disconnect")
    public ResponseEntity<String> disconnectDatabase() {
        databaseConnection
                .set(0);
        return ResponseEntity.ok(String.valueOf(format("Database %s disconnected", databaseUrl)));
    }
}
