package com.example.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/loadbalancer")
public class LoadBalancerDemoController {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @GetMapping("/test")
    public String testLoadBalancer() {
        // 调用10次服务，观察负载均衡效果
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            String response = restTemplate.getForObject("http://provider-service/api/users/test-loadbalancer", String.class);
            result.append(response).append("<br/>");
        }
        return result.toString();
    }
} 