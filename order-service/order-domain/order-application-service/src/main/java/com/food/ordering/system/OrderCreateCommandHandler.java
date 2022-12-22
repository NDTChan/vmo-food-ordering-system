package com.food.ordering.system;

import com.food.ordering.system.dto.create.CreateOrderCommand;
import com.food.ordering.system.dto.create.CreateOrderResponse;
import com.food.ordering.system.mapper.OrderDataMapper;
import com.food.ordering.system.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreateCommandHandler {

    private final OrderCreateHelper orderCreateHelper;
    private final OrderDataMapper orderDataMapper;

    private final OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher;

    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        var persistOrder = orderCreateHelper.persistOrder(createOrderCommand);
        log.info("createOrder with id: {}", persistOrder.getOrder().getId().getValue());
        orderCreatedPaymentRequestMessagePublisher.publish(persistOrder);
        return orderDataMapper.orderToCreateOrderResponse(persistOrder.getOrder(), "Order created successfully");
    }
}
