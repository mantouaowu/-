package com.example.gateway.fallback;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

/**
 * 全局服务降级处理器
 */
@RestController
public class GlobalFallbackProvider {
    
    /**
     * 用户服务降级处理
     */
    @RequestMapping("/fallback/user")
    public Mono<String> userServiceFallback() {
        return Mono.just("用户服务暂时不可用，请稍后再试！");
    }
    
    /**
     * 订单服务降级处理
     */
    @RequestMapping("/fallback/order")
    public Mono<String> orderServiceFallback() {
        return Mono.just("订单服务暂时不可用，请稍后再试！");
    }
    
    /**
     * 默认降级处理
     */
    @RequestMapping("/fallback/default")
    public Mono<String> defaultFallback() {
        return Mono.just("服务暂时不可用，请稍后再试！");
    }
} 