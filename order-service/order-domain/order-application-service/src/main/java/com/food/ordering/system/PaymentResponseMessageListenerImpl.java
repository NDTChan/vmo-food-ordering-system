package com.food.ordering.system;

import com.food.ordering.system.dto.message.PaymentResponse;
import com.food.ordering.system.event.OrderPaidEvent;
import com.food.ordering.system.ports.input.message.listener.payment.PaymentResponseMessageListener;
import com.food.ordering.system.saga.OrderPaymentSaga;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.food.ordering.system.entity.Order.FAILURE_MESSAGE_DELIMITER;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentResponseMessageListenerImpl implements PaymentResponseMessageListener {

    private final OrderPaymentSaga orderPaymentSaga;

    @Override
    public void paymentCompleted(PaymentResponse paymentResponse) {
        OrderPaidEvent domainEvent = orderPaymentSaga.process(paymentResponse);
        log.info("Publishing OrderPaidEvent for order id: {}", paymentResponse.getId());
        domainEvent.fire();
    }

    @Override
    public void paymentCancelled(PaymentResponse paymentResponse) {
        orderPaymentSaga.rollback(paymentResponse);
        log.info("Order is roll backed for order id: {} with failure messages: {}",
                paymentResponse.getOrderId(),
                String.join(FAILURE_MESSAGE_DELIMITER, paymentResponse.getFailureMessages()));
    }
}
