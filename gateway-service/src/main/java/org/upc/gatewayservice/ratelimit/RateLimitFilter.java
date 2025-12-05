package org.upc.gatewayservice.ratelimit;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(RateLimitFilter.class);

    private final RateLimitConfig config;
    private final MeterRegistry meterRegistry;

    private final Counter allowedRequests;
    private final Counter blockedRequests;
    private final Counter errorRequests;

    private static class RateLimitWindow {
        final long windowStartMillis;
        int requestCount;
        final int maxRequests;
        final long windowDurationMillis;

        RateLimitWindow(long windowStartMillis, int requestCount, int maxRequests, long windowDurationMillis) {
            this.windowStartMillis = windowStartMillis;
            this.requestCount = requestCount;
            this.maxRequests = maxRequests;
            this.windowDurationMillis = windowDurationMillis;
        }

        boolean isExpired(long currentTimeMillis) {
            return currentTimeMillis - windowStartMillis > windowDurationMillis;
        }

        int getRemainingRequests() {
            return Math.max(0, maxRequests - requestCount);
        }

        long getResetTimeMillis() {
            return windowStartMillis + windowDurationMillis;
        }
    }

    // Key = IP + ":" + Pattern/Scope
    private final Map<String, RateLimitWindow> requestsPerClient = new ConcurrentHashMap<>();

    private final ScheduledExecutorService cleanupExecutor;

    public RateLimitFilter(RateLimitConfig config, MeterRegistry meterRegistry) {
        this.config = config;
        this.meterRegistry = meterRegistry;

        this.allowedRequests = Counter.builder("gateway.ratelimit.allowed")
                .description("Number of allowed requests")
                .register(meterRegistry);

        this.blockedRequests = Counter.builder("gateway.ratelimit.blocked")
                .description("Number of blocked requests due to rate limiting")
                .register(meterRegistry);

        this.errorRequests = Counter.builder("gateway.ratelimit.errors")
                .description("Number of errors during rate limiting execution")
                .register(meterRegistry);

        this.cleanupExecutor = Executors.newSingleThreadScheduledExecutor();
        this.cleanupExecutor.scheduleAtFixedRate(
                this::cleanupExpiredWindows,
                1, 1, TimeUnit.MINUTES
        );
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Fail-safe: If config is disabled, pass through immediately
        if (config == null || !config.isEnabled()) {
            return chain.filter(exchange);
        }

        try {
            String clientIp = extractClientIp(exchange);
            String path = exchange.getRequest().getPath().value();

            // 1. Identificar qué regla aplica
            Map.Entry<String, RateLimitConfig.EndpointConfig> matchedRule = findMatchingRule(path);

            int maxRequests;
            long windowMillis;
            String limitKey;

            if (matchedRule != null && matchedRule.getValue() != null) {
                // Regla específica
                RateLimitConfig.EndpointConfig endpointConfig = matchedRule.getValue();
                maxRequests = endpointConfig.getMaxRequests();
                windowMillis = endpointConfig.getWindowSeconds() * 1000;
                limitKey = clientIp + ":" + matchedRule.getKey();
            } else {
                // Fallback global
                // Check for null global config just in case
                if (config.getGlobal() == null) {
                    log.error("Global rate limit config is null. Allowing request.");
                    return chain.filter(exchange);
                }
                maxRequests = config.getGlobal().getMaxRequests();
                windowMillis = config.getGlobal().getWindowSeconds() * 1000;
                limitKey = clientIp + ":global";
            }

            // Validar configuración para evitar ventanas de 0ms o 0 requests si no se desea
            if (windowMillis <= 0) windowMillis = 1000; // default 1s safety

            long now = Instant.now().toEpochMilli();
            final int finalMaxRequests = maxRequests;
            final long finalWindowMillis = windowMillis;

            // 3. Calcular ventana
            RateLimitWindow window = requestsPerClient.compute(limitKey, (key, currentWindow) -> {
                if (currentWindow == null || currentWindow.isExpired(now)) {
                    return new RateLimitWindow(now, 1, finalMaxRequests, finalWindowMillis);
                }
                currentWindow.requestCount++;
                return currentWindow;
            });

            // 4. Headers (add them regardless of outcome if enabled)
            if (config.isIncludeHeaders()) {
                addRateLimitHeaders(exchange, window);
            }

            // 5. Decisión
            if (window.requestCount > finalMaxRequests) {
                blockedRequests.increment();
                log.warn("Rate limit exceeded for key: {} ({}/{} requests)", limitKey, window.requestCount, finalMaxRequests);
                exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                return exchange.getResponse().setComplete();
            }

            allowedRequests.increment();
            return chain.filter(exchange);

        } catch (Exception e) {
            // Log error but allow traffic to avoid outage caused by the filter itself
            log.error("Error in RateLimitFilter: {}", e.getMessage(), e);
            errorRequests.increment();
            return chain.filter(exchange);
        }
    }

    private String extractClientIp(ServerWebExchange exchange) {
        try {
            String forwardedFor = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
            if (forwardedFor != null && !forwardedFor.isEmpty()) {
                return forwardedFor.split(",")[0].trim();
            }
            if (exchange.getRequest().getRemoteAddress() != null &&
                    exchange.getRequest().getRemoteAddress().getAddress() != null) {
                return exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
            }
        } catch (Exception e) {
            log.warn("Could not extract IP: {}", e.getMessage());
        }
        return "unknown";
    }

    private Map.Entry<String, RateLimitConfig.EndpointConfig> findMatchingRule(String path) {
        if (config.getEndpoints() == null) {
            return null;
        }
        return config.getEndpoints().entrySet().stream()
                .filter(entry -> pathMatches(path, entry.getKey()))
                .findFirst()
                .orElse(null);
    }

    private boolean pathMatches(String path, String pattern) {
        if (pattern == null || path == null) return false;
        String regex = pattern
                .replace("**", ".*")
                .replace("*", "[^/]*");
        return path.matches(regex);
    }

    private void addRateLimitHeaders(ServerWebExchange exchange, RateLimitWindow window) {
        try {
            exchange.getResponse().getHeaders().add("X-RateLimit-Limit", String.valueOf(window.maxRequests));
            exchange.getResponse().getHeaders().add("X-RateLimit-Remaining", String.valueOf(window.getRemainingRequests()));
            exchange.getResponse().getHeaders().add("X-RateLimit-Reset", String.valueOf(window.getResetTimeMillis() / 1000));
        } catch (Exception e) {
            log.warn("Failed to add headers: {}", e.getMessage());
        }
    }

    private void cleanupExpiredWindows() {
        try {
            long now = Instant.now().toEpochMilli();
            requestsPerClient.entrySet().removeIf(entry -> entry.getValue().isExpired(now));
        } catch (Exception e) {
            log.error("Error cleaning up rate limit windows", e);
        }
    }

    @Override
    public int getOrder() {
        return -10;
    }
}