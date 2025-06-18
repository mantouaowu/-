package com.example.serviceconsumer.feign;

import com.example.serviceconsumer.fallback.PriceFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-provider", path = "/products", contextId = "priceFeignClient", fallbackFactory = PriceFeignFallbackFactory.class)
public interface PriceFeignClient {

    @GetMapping("/price/{id}")
    Double getProductPrice(@PathVariable("id") Long id);
} 