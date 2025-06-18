package org.example.consumer2.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "service-provider")
public interface Provider1Service {
    @GetMapping("/api/hello")
    String hello();
} 