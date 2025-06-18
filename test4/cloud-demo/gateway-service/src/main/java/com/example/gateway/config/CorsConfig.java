package com.example.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Arrays;

/**
 * 全局跨域配置
 */
@Configuration
public class CorsConfig {
    
    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 允许特定的源访问，不使用"*"以支持credentials
        config.setAllowedOrigins(Arrays.asList("http://localhost:8080", "http://127.0.0.1:8080"));
        // 允许所有头信息
        config.addAllowedHeader("*");
        // 允许所有的HTTP方法（GET, POST, PUT, DELETE等）
        config.addAllowedMethod("*");
        // 允许携带认证信息
        config.setAllowCredentials(true);
        // 预检请求的有效期，单位秒
        config.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        // 对所有路径应用这个CORS配置
        source.registerCorsConfiguration("/**", config);
        
        return new CorsWebFilter(source);
    }
} 