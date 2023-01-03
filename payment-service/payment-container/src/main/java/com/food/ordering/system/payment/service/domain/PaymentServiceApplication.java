package com.food.ordering.system.payment.service.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.food.ordering.system.payment.data.access")
@EntityScan(basePackages = "com.food.ordering.system.payment.data.access")
@SpringBootApplication(scanBasePackages = "com.food.ordering")
public class PaymentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }

}
