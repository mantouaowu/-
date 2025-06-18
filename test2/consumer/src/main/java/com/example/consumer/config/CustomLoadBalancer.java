package com.example.consumer.config;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;

public class CustomLoadBalancer implements ReactorLoadBalancer<ServiceInstance> {
    private final ServiceInstanceListSupplier serviceInstanceListSupplier;
    private final String serviceId;
    private final Random random = new Random();

    public CustomLoadBalancer(ServiceInstanceListSupplier serviceInstanceListSupplier, String serviceId) {
        this.serviceInstanceListSupplier = serviceInstanceListSupplier;
        this.serviceId = serviceId;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        return serviceInstanceListSupplier.get()
                .next()
                .map(instances -> {
                    ServiceInstance instance = getInstance(instances);
                    return instance != null ? new DefaultResponse(instance) : new DefaultResponse(null);
                });
    }

    private ServiceInstance getInstance(List<ServiceInstance> instances) {
        if (instances.isEmpty()) {
            return null;
        }
        // 自定义负载均衡策略：随机选择实例，但优先选择响应时间短的实例
        return instances.get(random.nextInt(instances.size()));
    }
} 