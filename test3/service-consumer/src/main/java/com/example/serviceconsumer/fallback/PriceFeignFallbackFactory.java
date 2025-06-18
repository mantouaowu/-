package com.example.serviceconsumer.fallback;

import com.example.serviceconsumer.feign.PriceFeignClient;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PriceFeignFallbackFactory implements FallbackFactory<PriceFeignClient> {

    @Override
    public PriceFeignClient create(Throwable cause) {
        return new PriceFeignClient() {
            @Override
            public Double getProductPrice(Long id) {
                log.error("获取产品价格失败，触发服务降级。产品ID: {}，原因: {}", id, cause.getMessage());
                
                // 根据不同异常类型返回不同的降级信息
                if (cause instanceof RequestNotPermitted) {
                    log.error("限流器已触发，请求被拒绝");
                    return -1.0; // 表示被限流
                }
                
                return 0.0; // 默认降级返回价格为0
            }
        };
    }
} 