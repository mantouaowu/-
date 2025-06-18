package com.example.provider.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class ProviderController {

    @Value("${message.greeting}")
    private String greeting;

    @GetMapping("/greeting")
    public String getGreeting() {
        return greeting;
    }
    
    @GetMapping("/provider")
    public String provider() {
        return "这是服务提供者";
    }
} 