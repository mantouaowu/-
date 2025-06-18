package com.example.gateway.config;

import java.time.Duration;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;

/**
 * 使用Resilience4j限流器配置路由
 */
@Configuration
public class RateLimiterFilterConfig {

    /**
     * 通过编程方式配置路由和限流器
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // 用户服务路由
                .route("user-service-route", r -> r
                        .path("/user/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                // 熔断降级处理
                                .circuitBreaker(config -> config
                                        .setName("userCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/user"))
                                // 添加限流过滤器
                                .filter(userRateLimiterFilter()))
                        .uri("lb://user-service"))
                
                // 订单服务路由
                .route("order-service-route", r -> r
                        .path("/order/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                // 熔断降级处理
                                .circuitBreaker(config -> config
                                        .setName("orderCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/order"))
                                // 添加限流过滤器
                                .filter(orderRateLimiterFilter()))
                        .uri("lb://order-service"))
                .build();
    }

    /**
     * 用户服务限流过滤器
     */
    private GatewayFilter userRateLimiterFilter() {
        io.github.resilience4j.ratelimiter.RateLimiter rateLimiter = createRateLimiter("userRateLimiter", 1);
        return (exchange, chain) -> {
            return rateLimiter.executeSupplier(() -> chain.filter(exchange))
                    .onErrorResume(throwable -> {
                        exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                        return exchange.getResponse().setComplete();
                    });
        };
    }

    /**
     * 订单服务限流过滤器
     */
    private GatewayFilter orderRateLimiterFilter() {
        io.github.resilience4j.ratelimiter.RateLimiter rateLimiter = createRateLimiter("orderRateLimiter", 2);
        return (exchange, chain) -> {
            return rateLimiter.executeSupplier(() -> chain.filter(exchange))
                    .onErrorResume(throwable -> {
                        exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                        return exchange.getResponse().setComplete();
                    });
        };
    }

    /**
     * 创建限流器
     */
    private io.github.resilience4j.ratelimiter.RateLimiter createRateLimiter(String name, int limitForPeriod) {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .limitForPeriod(limitForPeriod)
                .timeoutDuration(Duration.ofMillis(500))
                .build();
        
        RateLimiterRegistry registry = RateLimiterRegistry.of(config);
        return registry.rateLimiter(name);
    }
} 