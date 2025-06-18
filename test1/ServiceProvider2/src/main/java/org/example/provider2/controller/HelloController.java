package org.example.provider2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {
    
    @GetMapping("/hello")
    public String hello() {
        return "Hello from Service Provider 2!";
    }
    
    @GetMapping("/info")
    public String info() {
        return "This is Service Provider 2 running on port 8082";
    }
} 