package org.example.consumer.controller;

import org.example.consumer.feign.HelloService;
import org.example.consumer.feign.Provider2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ConsumerController {
    
    @Autowired
    private HelloService helloService;
    
    @Autowired
    private Provider2Service provider2Service;
    
    @GetMapping("/hello")
    public String hello() {
        return helloService.hello();
    }
    
    @GetMapping("/provider2")
    public String callProvider2() {
        return "Consumer1 calling Provider2: " + provider2Service.hello();
    }
    
    @GetMapping("/provider2/info")
    public String getProvider2Info() {
        return "Info from Provider2: " + provider2Service.info();
    }
} 