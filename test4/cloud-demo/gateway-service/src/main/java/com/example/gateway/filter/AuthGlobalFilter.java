package com.example.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * 全局权限认证过滤器
 */
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private static final String AUTH_TOKEN = "token";
    private static final String VALID_TOKEN = "123456";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        MultiValueMap<String, String> queryParams = request.getQueryParams();

        // 获取token参数
        String token = queryParams.getFirst(AUTH_TOKEN);

        // 如果请求中没有token参数，或者token值不正确，则返回401错误
        if (token == null || !VALID_TOKEN.equals(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // token验证通过，继续执行后续过滤器
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        // 设置过滤器优先级，数字越小优先级越高
        return 0;
    }
}