package com.example.gateway.handler;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 全局异常处理
 */
@Configuration
public class RateLimiterExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        
        if (ex instanceof ResponseStatusException) {
            response.setStatusCode(((ResponseStatusException) ex).getStatusCode());
        } else if (ex instanceof NotFoundException) {
            response.setStatusCode(HttpStatus.NOT_FOUND);
        } else if (ex.getMessage() != null && ex.getMessage().contains("RateLimiterException")) {
            // 处理Resilience4j限流异常
            return handleRateLimitException(exchange);
        } else {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        // 设置返回类型为JSON
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        
        // 构建错误信息
        String errorMsg = "{\"code\":" + response.getStatusCode().value() + ",\"message\":\"" + ex.getMessage() + "\"}";
        DataBuffer buffer = response.bufferFactory().wrap(errorMsg.getBytes(StandardCharsets.UTF_8));
        
        // 写入响应
        return response.writeWith(Mono.just(buffer));
    }
    
    /**
     * 处理限流异常
     */
    private Mono<Void> handleRateLimitException(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS); // 429状态码
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        
        String errorMsg = "{\"code\":429,\"message\":\"操作太频繁，请稍后再试！\"}";
        DataBuffer buffer = response.bufferFactory().wrap(errorMsg.getBytes(StandardCharsets.UTF_8));
        
        return response.writeWith(Mono.just(buffer));
    }
} 