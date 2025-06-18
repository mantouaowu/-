package com.example.consumer.feign;

import com.example.common.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "provider-service")
public interface UserFeignClient {
    @GetMapping("/api/users/{id}")
    User getUser(@PathVariable("id") Long id);

    @GetMapping("/api/users")
    List<User> getAllUsers();

    @PostMapping("/api/users")
    User createUser(@RequestBody User user);

    @PutMapping("/api/users/{id}")
    User updateUser(@PathVariable("id") Long id, @RequestBody User user);

    @DeleteMapping("/api/users/{id}")
    void deleteUser(@PathVariable("id") Long id);
} 