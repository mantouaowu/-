package com.example.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/hello")
public class HelloController {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @GetMapping("/rest")
    public String helloRest() {
        return restTemplate.getForObject("http://provider-service/api/hello", String.class);
    }
    
    @GetMapping("/local")
    public String helloLocal() {
        return "Hello from Consumer Service";
    }
} 