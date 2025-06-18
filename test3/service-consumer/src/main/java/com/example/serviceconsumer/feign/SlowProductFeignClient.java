package com.example.serviceconsumer.feign;

import com.example.serviceconsumer.entity.Product;
import com.example.serviceconsumer.fallback.SlowProductFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-provider", path = "/products", contextId = "slowProductFeignClient", fallbackFactory = SlowProductFeignFallbackFactory.class)
public interface SlowProductFeignClient {

    @GetMapping("/slow/{id}")
    Product getProductByIdWithDelay(@PathVariable("id") Long id);
} 