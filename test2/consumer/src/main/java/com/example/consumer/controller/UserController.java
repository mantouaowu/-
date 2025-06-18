package com.example.consumer.controller;

import com.example.common.entity.User;
import com.example.consumer.feign.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private RestTemplate restTemplate;
    // 使用RestTemplate的接口
    @GetMapping("/rest/{id}")
    public User getUserByRest(@PathVariable(name = "id") Long id) {
        return restTemplate.getForObject("http://provider-service/api/users/" + id, User.class);
    }
    @GetMapping("/rest")
    public List<User> getAllUsersByRest() {
        return restTemplate.getForObject("http://provider-service/api/users", List.class);
    }
    @PostMapping("/rest")
    public User createUserByRest(@RequestBody User user) {
        return restTemplate.postForObject("http://provider-service/api/users", user, User.class);
    }
    @PutMapping("/rest/{id}")
    public User updateUserByRest(@PathVariable(name = "id") Long id, @RequestBody User user) {
        restTemplate.put("http://provider-service/api/users/" + id, user);
        return user;
    }
    @DeleteMapping("/rest/{id}")
    public void deleteUserByRest(@PathVariable(name = "id") Long id) {
        restTemplate.delete("http://provider-service/api/users/" + id);
    }
    // 使用Feign的接口
    @GetMapping("/feign/{id}")
    public User getUserByFeign(@PathVariable(name = "id") Long id) {
        return userFeignClient.getUser(id);
    }

    @GetMapping("/feign")
    public List<User> getAllUsersByFeign() {
        return userFeignClient.getAllUsers();
    }

    @PostMapping("/feign")
    public User createUserByFeign(@RequestBody User user) {
        return userFeignClient.createUser(user);
    }

    @PutMapping("/feign/{id}")
    public User updateUserByFeign(@PathVariable(name = "id") Long id, @RequestBody User user) {
        return userFeignClient.updateUser(id, user);
    }

    @DeleteMapping("/feign/{id}")
    public void deleteUserByFeign(@PathVariable(name = "id") Long id) {
        userFeignClient.deleteUser(id);
    }
} 