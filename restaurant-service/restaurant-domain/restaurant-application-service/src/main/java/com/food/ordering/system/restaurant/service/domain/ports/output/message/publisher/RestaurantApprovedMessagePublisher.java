package com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.restaurant.domain.core.event.OrderApprovalEvent;

public interface RestaurantApprovedMessagePublisher extends DomainEventPublisher<OrderApprovalEvent> {

}
