package com.food.ordering.system.ports.output.message.publisher.payment;


import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.outbox.model.payment.OrderPaymentOutboxMessage;

import java.util.function.BiConsumer;

public interface PaymentRequestMessagePublisher {

    void publish(OrderPaymentOutboxMessage message,
                 BiConsumer<OrderPaymentOutboxMessage, OutboxStatus> outboxCallback);

}
