package org.example.consumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "service-provider2")
public interface Provider2Service {
    @GetMapping("/api/hello")
    String hello();
    
    @GetMapping("/api/info")
    String info();
} 