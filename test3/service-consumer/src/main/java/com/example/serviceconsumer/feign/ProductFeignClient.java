package com.example.serviceconsumer.feign;

import com.example.serviceconsumer.entity.Product;
import com.example.serviceconsumer.fallback.ProductFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "service-provider", path = "/products", fallbackFactory = ProductFeignFallbackFactory.class)
public interface ProductFeignClient {

    @GetMapping
    List<Product> getAllProducts();

    @GetMapping("/{id}")
    Product getProductById(@PathVariable("id") Long id);
} 