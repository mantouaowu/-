package com.example.provider.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hello")
public class HelloController {
    
    @Value("${server.port}")
    private String serverPort;
    
    @GetMapping
    public String hello() {
        return "Hello from Provider Service on port: " + serverPort;
    }
} 