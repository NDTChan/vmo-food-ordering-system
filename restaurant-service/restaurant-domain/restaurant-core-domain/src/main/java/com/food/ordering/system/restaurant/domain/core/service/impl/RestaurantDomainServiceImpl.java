package com.food.ordering.system.restaurant.domain.core.service.impl;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.domain.valueobject.OrderApprovalStatus;
import com.food.ordering.system.restaurant.domain.core.entity.Restaurant;
import com.food.ordering.system.restaurant.domain.core.event.OrderApprovalEvent;
import com.food.ordering.system.restaurant.domain.core.event.OrderApprovedEvent;
import com.food.ordering.system.restaurant.domain.core.event.OrderRejectedEvent;
import com.food.ordering.system.restaurant.domain.core.service.RestaurantDomainService;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.food.ordering.system.domain.DomainConstants.UTC;

@Slf4j
public class RestaurantDomainServiceImpl implements RestaurantDomainService {

    @Override
    public OrderApprovalEvent validateOrder(Restaurant restaurant,
                                            List<String> failureMessages,
                                            DomainEventPublisher<OrderApprovalEvent> orderApprovalEventDomainEventPublisher,
                                            DomainEventPublisher<OrderRejectedEvent> orderRejectedEventDomainEventPublisher) {
        restaurant.validateOrder(failureMessages);
        log.info("Order validation with id {}", restaurant.getOrderDetail().getId());
        if (failureMessages.isEmpty()) {
            log.info("Order validation with id {} is successful", restaurant.getOrderDetail().getId());
            restaurant.constructOrderApproval(OrderApprovalStatus.APPROVED);
            return new OrderApprovedEvent(restaurant.getOrderApproval(), restaurant.getId(),
                    failureMessages, ZonedDateTime.now(ZoneId.of(UTC)), orderApprovalEventDomainEventPublisher);
        } else {
            log.info("Order validation with id {} is failed", restaurant.getOrderDetail().getId());
            restaurant.constructOrderApproval(OrderApprovalStatus.REJECTED);
            return new OrderRejectedEvent(restaurant.getOrderApproval(), restaurant.getId(),
                    failureMessages, ZonedDateTime.now(), orderRejectedEventDomainEventPublisher);
        }

    }
}
