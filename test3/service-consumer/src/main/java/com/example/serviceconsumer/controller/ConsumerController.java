package com.example.serviceconsumer.controller;

import com.example.serviceconsumer.entity.Product;
import com.example.serviceconsumer.feign.PriceFeignClient;
import com.example.serviceconsumer.feign.ProductFeignClient;
import com.example.serviceconsumer.feign.SlowProductFeignClient;
import com.example.serviceconsumer.feign.StockFeignClient;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/consumer")
@RequiredArgsConstructor
public class ConsumerController {

    private final ProductFeignClient productFeignClient;
    private final SlowProductFeignClient slowProductFeignClient;
    private final StockFeignClient stockFeignClient;
    private final PriceFeignClient priceFeignClient;

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productFeignClient.getAllProducts();
    }

    // 使用断路器A进行保护
    @GetMapping("/products/{id}")
    @CircuitBreaker(name = "circuitBreakerA", fallbackMethod = "getProductFallback")
    public Product getProduct(@PathVariable Long id) {
        log.info("正在调用服务获取产品信息，产品ID: {}", id);
        return productFeignClient.getProductById(id);
    }

    // 断路器A的服务降级方法
    public Product getProductFallback(Long id, Exception ex) {
        log.error("断路器A触发服务降级，产品ID: {}, 异常: {}", id, ex.getMessage());
        return new Product(0L, "断路器A服务降级 - 控制器层", 0.0, 0);
    }

    // 使用断路器B进行保护（慢调用）
    @GetMapping("/slow-products/{id}")
    @CircuitBreaker(name = "circuitBreakerB", fallbackMethod = "getSlowProductFallback")
    public Product getSlowProduct(@PathVariable Long id) {
        log.info("正在调用慢服务获取产品信息，产品ID: {}", id);
        return slowProductFeignClient.getProductByIdWithDelay(id);
    }

    // 断路器B的服务降级方法
    public Product getSlowProductFallback(Long id, Exception ex) {
        log.error("断路器B触发服务降级，产品ID: {}, 异常: {}", id, ex.getMessage());
        return new Product(0L, "断路器B服务降级（慢调用） - 控制器层", 0.0, 0);
    }

    // 使用隔离器进行保护
    @GetMapping("/stocks/{id}")
    @Bulkhead(name = "bulkheadService", fallbackMethod = "getStockFallback")
    public Map<String, Object> getStock(@PathVariable Long id) {
        log.info("正在调用服务获取产品库存，产品ID: {}", id);
        Integer stock = stockFeignClient.getProductStock(id);
        
        Map<String, Object> result = new HashMap<>();
        result.put("productId", id);
        result.put("stock", stock);
        result.put("timestamp", System.currentTimeMillis());
        
        return result;
    }

    // 隔离器的服务降级方法
    public Map<String, Object> getStockFallback(Long id, Exception ex) {
        log.error("隔离器触发服务降级，产品ID: {}, 异常: {}", id, ex.getMessage());
        
        Map<String, Object> result = new HashMap<>();
        result.put("productId", id);
        result.put("stock", -999); // 隔离器降级特殊值
        result.put("message", "隔离器已满，请稍后重试");
        result.put("timestamp", System.currentTimeMillis());
        
        return result;
    }

    // 使用限流器进行保护
    @GetMapping("/prices/{id}")
    @RateLimiter(name = "rateLimiterService", fallbackMethod = "getPriceFallback")
    public Map<String, Object> getPrice(@PathVariable Long id) {
        log.info("正在调用服务获取产品价格，产品ID: {}", id);
        Double price = priceFeignClient.getProductPrice(id);
        
        Map<String, Object> result = new HashMap<>();
        result.put("productId", id);
        result.put("price", price);
        result.put("timestamp", System.currentTimeMillis());
        
        return result;
    }

    // 限流器的服务降级方法
    public Map<String, Object> getPriceFallback(Long id, Exception ex) {
        log.error("限流器触发服务降级，产品ID: {}, 异常: {}", id, ex.getMessage());
        
        Map<String, Object> result = new HashMap<>();
        result.put("productId", id);
        result.put("price", -999.0); // 限流器降级特殊值
        result.put("message", "请求过于频繁，请稍后重试");
        result.put("timestamp", System.currentTimeMillis());
        
        return result;
    }
} 