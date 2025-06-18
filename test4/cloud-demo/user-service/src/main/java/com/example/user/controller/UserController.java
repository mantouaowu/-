package com.example.user.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Value("${server.port}")
    private String serverPort;
    
    @GetMapping("/info/{id}")
    public String getUserInfo(@PathVariable("id") String id) {
        return "用户ID: " + id + ", 来自用户服务, 端口: " + serverPort;
    }
    
    @GetMapping("/hello")
    public String hello() {
        return "Hello from User Service on port: " + serverPort;
    }
} 