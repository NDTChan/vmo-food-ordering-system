package com.food.ordering.system.ports.output.message.publisher.restaurantapproval;


import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.outbox.model.approval.OrderApprovalOutboxMessage;

import java.util.function.BiConsumer;

public interface RestaurantApprovalRequestMessagePublisher {

    void publish(OrderApprovalOutboxMessage message,
                 BiConsumer<OrderApprovalOutboxMessage, OutboxStatus> outboxCallback);

}
