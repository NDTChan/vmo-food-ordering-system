package com.food.ordering.system.ports.output.repository;

import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.entity.Order;
import com.food.ordering.system.valueobject.TrackingId;

import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findById(OrderId trackingId);

    Optional<Order> findByTrackingId(TrackingId trackingId);
}
