package com.jonnymatts.prometheus.demo;

import io.prometheus.client.spring.web.PrometheusTimeMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class StatusController {

    @RequestMapping(path = "successCode")
    @PrometheusTimeMethod(name = "success_seconds", help="Number of seconds taken to respond with a success response")
    public ResponseEntity<Integer> success() {
        return getIntegerResponseEntity(200, 204);
    }

    @RequestMapping(path = "badCode")
    @PrometheusTimeMethod(name = "bad_request_seconds", help="Number of seconds taken to respond with a bad request response")
    public ResponseEntity<Integer> bad() {
        return getIntegerResponseEntity(400, 409);
    }

    @RequestMapping(path = "errorCode")
    @PrometheusTimeMethod(name = "error_seconds", help="Number of seconds taken to respond with an error response")
    public ResponseEntity<Integer> error() {
        return getIntegerResponseEntity(500, 504);
    }

    private ResponseEntity<Integer> getIntegerResponseEntity(int origin, int bound) {
        final int status = new Random().ints(1, origin, bound).findFirst().getAsInt();
        return ResponseEntity.status(status).body(status);
    }
}
