package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.service.OrderDomainService;
import com.food.ordering.system.service.impl.OrderDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public OrderDomainService orderDomainService() {
        return new OrderDomainServiceImpl();
    }
}
