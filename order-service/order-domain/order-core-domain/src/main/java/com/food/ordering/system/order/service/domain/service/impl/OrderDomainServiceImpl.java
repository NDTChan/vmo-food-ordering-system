package com.food.ordering.system.order.service.domain.service.impl;

import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.service.OrderDomainService;

import java.util.List;

public class OrderDomainServiceImpl implements OrderDomainService {
    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
        return null;
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        return null;
    }

    @Override
    public void approve(Order order) {

    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        return null;
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {

    }
}
