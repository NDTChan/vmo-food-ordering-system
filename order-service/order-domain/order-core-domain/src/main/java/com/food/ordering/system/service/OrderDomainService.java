package com.food.ordering.system.service;

import com.food.ordering.system.entity.Restaurant;
import com.food.ordering.system.event.OrderCancelledEvent;
import com.food.ordering.system.event.OrderCreatedEvent;
import com.food.ordering.system.entity.Order;
import com.food.ordering.system.event.OrderPaidEvent;

import java.util.List;

public interface OrderDomainService {
    OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant);

    OrderPaidEvent payOrder(Order order);

    void approve(Order order);

    OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages);

    void cancelOrder(Order order, List<String> failureMessages);
}
