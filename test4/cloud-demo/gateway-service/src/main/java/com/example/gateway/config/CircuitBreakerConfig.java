package com.example.gateway.config;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

/**
 * 熔断器配置
 */
@Configuration
public class CircuitBreakerConfig {

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.custom()
                        // 熔断器关闭状态下，连续错误请求数达到此阈值则打开熔断器
                        .failureRateThreshold(50)
                        // 熔断器打开状态等待时间，之后进入半开状态
                        .waitDurationInOpenState(Duration.ofSeconds(10))
                        // 熔断器半开状态下的测试请求数
                        .permittedNumberOfCallsInHalfOpenState(5)
                        // 滑动窗口类型，基于计数或时间
                        .slidingWindowType(SlidingWindowType.COUNT_BASED)
                        // 滑动窗口大小
                        .slidingWindowSize(10)
                        // 进入熔断器开启状态的最小调用次数
                        .minimumNumberOfCalls(5)
                        .build())
                .timeLimiterConfig(TimeLimiterConfig.custom()
                        // 请求超时时间
                        .timeoutDuration(Duration.ofSeconds(5))
                        .build())
                .build()
        );
    }
} 