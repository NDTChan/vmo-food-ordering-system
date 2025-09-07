package com.food.ordering.system;

import com.food.ordering.system.dto.create.CreateOrderCommand;
import com.food.ordering.system.dto.create.CreateOrderResponse;
import com.food.ordering.system.helper.OrderCreateHelper;
import com.food.ordering.system.helper.OrderSagaHelper;
import com.food.ordering.system.mapper.OrderDataMapper;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.outbox.scheduler.payment.PaymentOutboxHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreateCommandHandler {

    private final OrderCreateHelper orderCreateHelper;
    private final OrderDataMapper orderDataMapper;
    private final PaymentOutboxHelper paymentOutboxHelper;
    private final OrderSagaHelper orderSagaHelper;
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        var persistOrder = orderCreateHelper.persistOrder(createOrderCommand);
        log.info("createOrder with id: {}", persistOrder.getOrder().getId().getValue());
        var response = orderDataMapper.orderToCreateOrderResponse(persistOrder.getOrder(),"Order created successfully");

        paymentOutboxHelper.savePaymentOutboxMessage(
                orderDataMapper.orderCreatedEventToOrderPaymentEventPayload(persistOrder),
                persistOrder.getOrder().getStatus(),
                orderSagaHelper.orderStatusToSagaStatus(persistOrder.getOrder().getStatus()),
                OutboxStatus.STARTED,
                UUID.randomUUID()
        );

        log.info("Returning CreateOrderResponse with order id : {}", persistOrder.getOrder().getId());

        return response;
    }
}
