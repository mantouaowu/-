package org.example.consumer2.controller;

import org.example.consumer2.feign.Provider1Service;
import org.example.consumer2.feign.Provider2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {
    
    @Autowired
    private Provider1Service provider1Service;
    
    @Autowired
    private Provider2Service provider2Service;
    
    @GetMapping("/provider1")
    public String callProvider1() {
        return "Consumer2 calling Provider1: " + provider1Service.hello();
    }
    
    @GetMapping("/provider2")
    public String callProvider2() {
        return "Consumer2 calling Provider2: " + provider2Service.hello();
    }
    
    @GetMapping("/provider2/info")
    public String getProvider2Info() {
        return "Info from Provider2: " + provider2Service.info();
    }
} 