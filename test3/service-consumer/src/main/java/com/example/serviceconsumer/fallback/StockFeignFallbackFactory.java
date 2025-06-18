package com.example.serviceconsumer.fallback;

import com.example.serviceconsumer.feign.StockFeignClient;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StockFeignFallbackFactory implements FallbackFactory<StockFeignClient> {

    @Override
    public StockFeignClient create(Throwable cause) {
        return new StockFeignClient() {
            @Override
            public Integer getProductStock(Long id) {
                log.error("获取产品库存失败，触发服务降级。产品ID: {}，原因: {}", id, cause.getMessage());
                
                // 根据不同异常类型返回不同的降级信息
                if (cause instanceof BulkheadFullException) {
                    log.error("隔离器已满，请求被拒绝");
                    return -1; // 表示隔离器已满
                }
                
                return 0; // 默认降级返回库存为0
            }
        };
    }
} 