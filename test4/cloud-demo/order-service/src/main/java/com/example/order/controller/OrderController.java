package com.example.order.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
    
    @Value("${server.port}")
    private String serverPort;
    
    @GetMapping("/info/{id}")
    public String getOrderInfo(@PathVariable("id") String id) {
        return "订单ID: " + id + ", 来自订单服务, 端口: " + serverPort;
    }
    
    @GetMapping("/hello")
    public String hello() {
        return "Hello from Order Service on port: " + serverPort;
    }
} 