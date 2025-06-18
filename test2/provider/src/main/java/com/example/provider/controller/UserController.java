package com.example.provider.controller;

import com.example.common.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final ConcurrentHashMap<Long, User> userMap = new ConcurrentHashMap<>();
    
    @Value("${server.port}")
    private String serverPort;

    public UserController() {
        // 初始化一些测试数据
        User user1 = new User();
        user1.setId(1L);
        user1.setName("张三");
        user1.setEmail("zhangsan@example.com");
        user1.setAge(25);
        
        User user2 = new User();
        user2.setId(2L);
        user2.setName("李四");
        user2.setEmail("lisi@example.com");
        user2.setAge(30);
        
        userMap.put(user1.getId(), user1);
        userMap.put(user2.getId(), user2);
    }

    @GetMapping("/test-loadbalancer")
    public String testLoadBalancer() {
        return "来自服务器端口: " + serverPort + " 的响应";
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable(name = "id") Long id) {
        User user = userMap.get(id);
        if (user != null) {
            // 创建一个新的User对象，避免修改原始对象
            User responseUser = new User();
            responseUser.setId(user.getId());
            responseUser.setName(user.getName() + " (from port: " + serverPort + ")");
            responseUser.setEmail(user.getEmail());
            responseUser.setAge(user.getAge());
            return responseUser;
        }
        return null;
    }

    @GetMapping
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        for (User user : userMap.values()) {
            User responseUser = new User();
            responseUser.setId(user.getId());
            responseUser.setName(user.getName() + " (from port: " + serverPort + ")");
            responseUser.setEmail(user.getEmail());
            responseUser.setAge(user.getAge());
            users.add(responseUser);
        }
        return users;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        userMap.put(user.getId(), user);
        User responseUser = new User();
        responseUser.setId(user.getId());
        responseUser.setName(user.getName() + " (from port: " + serverPort + ")");
        responseUser.setEmail(user.getEmail());
        responseUser.setAge(user.getAge());
        return responseUser;
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable(name = "id") Long id, @RequestBody User user) {
        user.setId(id);
        userMap.put(id, user);
        User responseUser = new User();
        responseUser.setId(user.getId());
        responseUser.setName(user.getName() + " (from port: " + serverPort + ")");
        responseUser.setEmail(user.getEmail());
        responseUser.setAge(user.getAge());
        return responseUser;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "id") Long id) {
        userMap.remove(id);
        return ResponseEntity.ok("用户删除成功");
    }
} 