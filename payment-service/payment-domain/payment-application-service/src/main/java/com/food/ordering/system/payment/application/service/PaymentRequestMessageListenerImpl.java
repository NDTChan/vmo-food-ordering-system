package com.food.ordering.system.payment.application.service;

import com.food.ordering.system.payment.application.service.dto.PaymentRequest;
import com.food.ordering.system.payment.application.service.ports.input.message.listener.PaymentRequestMessageListener;
import com.food.ordering.system.payment.application.service.ports.output.message.publisher.PaymentCancelledMessagePublisher;
import com.food.ordering.system.payment.application.service.ports.output.message.publisher.PaymentCompletedMessagePublisher;
import com.food.ordering.system.payment.application.service.ports.output.message.publisher.PaymentFailedMessagePublisher;
import com.food.ordering.system.payment.service.domain.event.PaymentCancelledEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentCompletedEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentFailedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentRequestMessageListenerImpl implements PaymentRequestMessageListener {

    private final PaymentRequestHelper paymentRequestHelper;
    private final PaymentCompletedMessagePublisher paymentCompletedMessagePublisher;
    private final PaymentCancelledMessagePublisher paymentCancelledMessagePublisher;
    private final PaymentFailedMessagePublisher paymentFailedMessagePublisher;

    @Override
    public void completePayment(PaymentRequest paymentRequest) {
        PaymentEvent paymentEvent = paymentRequestHelper.persistPayment(paymentRequest);
        fireEvent(paymentEvent);
    }

    @Override
    public void cancelPayment(PaymentRequest paymentRequest) {
        PaymentEvent paymentEvent = paymentRequestHelper.persistCancelPayment(paymentRequest);
        fireEvent(paymentEvent);
    }

    private void fireEvent(PaymentEvent paymentEvent) {
        log.info("Publishing payment event with payment id: {} and order id: {}",
                paymentEvent.getPayment().getId().getValue(),
                paymentEvent.getPayment().getOrderId().getValue());
        if (paymentEvent instanceof PaymentCompletedEvent) {
            paymentCompletedMessagePublisher.publish((PaymentCompletedEvent) paymentEvent);
        } else if (paymentEvent instanceof PaymentCancelledMessagePublisher) {
            paymentCancelledMessagePublisher.publish((PaymentCancelledEvent) paymentEvent);
        } else if (paymentEvent instanceof PaymentFailedMessagePublisher) {
            paymentFailedMessagePublisher.publish((PaymentFailedEvent) paymentEvent);
        }
    }

}
