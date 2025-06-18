package com.example.serviceprovider.controller;

import com.example.serviceprovider.entity.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final List<Product> productList = Arrays.asList(
            new Product(1L, "iPhone 15", 7999.0, 100),
            new Product(2L, "MacBook Pro", 12999.0, 50),
            new Product(3L, "iPad Pro", 6999.0, 80)
    );

    @GetMapping
    public List<Product> getAllProducts() {
        return productList;
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) throws InterruptedException {
        // 随机模拟服务延迟或失败，用于测试断路器
        simulateRandomBehavior();
        
        return productList.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
    
    @GetMapping("/slow/{id}")
    public Product getProductByIdWithDelay(@PathVariable Long id) throws InterruptedException {
        // 模拟慢调用，用于测试断路器B
        TimeUnit.SECONDS.sleep(3);
        
        return productList.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
    
    @GetMapping("/stock/{id}")
    public Integer getProductStock(@PathVariable Long id) throws InterruptedException {
        // 用于测试隔离器
        TimeUnit.MILLISECONDS.sleep(50);
        
        return productList.stream()
                .filter(product -> product.getId().equals(id))
                .map(Product::getStock)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
    
    @GetMapping("/price/{id}")
    public Double getProductPrice(@PathVariable Long id) {
        // 用于测试限流器
        return productList.stream()
                .filter(product -> product.getId().equals(id))
                .map(Product::getPrice)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
    
    private void simulateRandomBehavior() throws InterruptedException {
        Random random = new Random();
        int randomNum = random.nextInt(10);
        
        if (randomNum < 4) {
            // 40% 概率抛出异常
            throw new RuntimeException("Service temporarily unavailable");
        } else if (randomNum < 7) {
            // 30% 概率延迟
            TimeUnit.MILLISECONDS.sleep(2500);
        }
        // 30% 概率正常响应
    }
} 