package org.upc.gatewayservice.ratelimit;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * REST endpoint for rate limiting configuration and statistics.
 */
@RestController
@RequestMapping("/actuator/rate-limit")
public class RateLimitController {

    private final RateLimitConfig config;

    public RateLimitController(RateLimitConfig config) {
        this.config = config;
    }

    /**
     * Obtains the current rate limiting configuration
     *
     * @return the rate limiting settings
     */
    @GetMapping("/config")
    public ResponseEntity<Map<String, Object>> getConfig() {
        Map<String, Object> response = new HashMap<>();

        response.put("enabled", config.isEnabled());
        response.put("includeHeaders", config.isIncludeHeaders());

        Map<String, Object> globalConfig = new HashMap<>();
        globalConfig.put("maxRequests", config.getGlobal().getMaxRequests());
        globalConfig.put("windowSeconds", config.getGlobal().getWindowSeconds());
        response.put("global", globalConfig);

        Map<String, Map<String, Object>> endpointsConfig = new HashMap<>();
        config.getEndpoints().forEach((path, endpointConfig) -> {
            Map<String, Object> endpointData = new HashMap<>();
            endpointData.put("maxRequests", endpointConfig.getMaxRequests());
            endpointData.put("windowSeconds", endpointConfig.getWindowSeconds());
            endpointsConfig.put(path, endpointData);
        });
        response.put("endpoints", endpointsConfig);

        return ResponseEntity.ok(response);
    }

    /**
     * Obtains current rate limiting status
     *
     * @return the rate limiting status
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("enabled", config.isEnabled());
        stats.put("message", config.isEnabled()
                ? "Rate limiting is active and protecting your APIs"
                : "Rate limiting is disabled");

        return ResponseEntity.ok(stats);
    }
}
