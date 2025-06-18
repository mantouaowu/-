package com.example.serviceconsumer.fallback;

import com.example.serviceconsumer.entity.Product;
import com.example.serviceconsumer.feign.SlowProductFeignClient;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SlowProductFeignFallbackFactory implements FallbackFactory<SlowProductFeignClient> {

    @Override
    public SlowProductFeignClient create(Throwable cause) {
        return new SlowProductFeignClient() {
            @Override
            public Product getProductByIdWithDelay(Long id) {
                log.error("慢调用获取产品详情失败，触发服务降级。产品ID: {}，原因: {}", id, cause.getMessage());
                
                // 根据不同异常类型返回不同的降级信息
                if (cause instanceof CallNotPermittedException) {
                    log.error("断路器B开启，请求被拒绝");
                    return new Product(0L, "断路器B已开启，服务暂时不可用（慢调用）", 0.0, 0);
                }
                
                return new Product(0L, "服务降级，暂时无法获取产品信息（慢调用）", 0.0, 0);
            }
        };
    }
} 