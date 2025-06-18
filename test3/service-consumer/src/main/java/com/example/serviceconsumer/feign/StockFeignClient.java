package com.example.serviceconsumer.feign;

import com.example.serviceconsumer.fallback.StockFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-provider", path = "/products", contextId = "stockFeignClient", fallbackFactory = StockFeignFallbackFactory.class)
public interface StockFeignClient {

    @GetMapping("/stock/{id}")
    Integer getProductStock(@PathVariable("id") Long id);
} 