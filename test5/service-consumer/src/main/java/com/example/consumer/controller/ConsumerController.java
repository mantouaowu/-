package com.example.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.consumer.client.ProviderClient;

@RestController
@RefreshScope
public class ConsumerController {

    @Value("${message.welcome}")
    private String welcome;

    @Autowired
    private ProviderClient providerClient;

    @GetMapping("/welcome")
    public String getWelcome() {
        return welcome;
    }

    @GetMapping("/consumer")
    public String consumer() {
        return "这是服务消费者";
    }

    @GetMapping("/callProvider")
    public String callProvider() {
        return "服务消费者调用服务提供者：" + providerClient.getProvider();
    }

    @GetMapping("/callGreeting")
    public String callGreeting() {
        return "服务消费者调用服务提供者的问候：" + providerClient.getGreeting();
    }
} 