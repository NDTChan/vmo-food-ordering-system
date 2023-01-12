package com.food.ordering.system.restaurant.container;

import com.food.ordering.system.restaurant.domain.core.service.RestaurantDomainService;
import com.food.ordering.system.restaurant.domain.core.service.impl.RestaurantDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public RestaurantDomainService restaurantDomainService() {
        return new RestaurantDomainServiceImpl();
    }
}
