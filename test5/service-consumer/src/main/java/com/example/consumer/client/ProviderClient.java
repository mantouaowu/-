package com.example.consumer.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "service-provider")
public interface ProviderClient {
    
    @GetMapping("/greeting")
    String getGreeting();
    
    @GetMapping("/provider")
    String getProvider();
} 