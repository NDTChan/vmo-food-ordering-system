package com.food.ordering.system.payment.application.service;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.payment.application.service.dto.PaymentRequest;
import com.food.ordering.system.payment.application.service.exception.PaymentApplicationServiceException;
import com.food.ordering.system.payment.application.service.mapper.PaymentDataMapper;
import com.food.ordering.system.payment.application.service.ports.output.repository.CreditEntryRepository;
import com.food.ordering.system.payment.application.service.ports.output.repository.CreditHistoryRepository;
import com.food.ordering.system.payment.application.service.ports.output.repository.PaymentRepository;
import com.food.ordering.system.payment.service.domain.entity.CreditEntry;
import com.food.ordering.system.payment.service.domain.entity.CreditHistory;
import com.food.ordering.system.payment.service.domain.entity.Payment;
import com.food.ordering.system.payment.service.domain.event.PaymentCancelledEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentCompletedEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentFailedEvent;
import com.food.ordering.system.payment.service.domain.exception.PaymentNotFoundException;
import com.food.ordering.system.payment.service.domain.service.PaymentDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentRequestHelper {
    private final PaymentDomainService paymentDomainService;
    private final PaymentDataMapper paymentDataMapper;
    private final PaymentRepository paymentRepository;
    private final CreditHistoryRepository creditHistoryRepository;
    private final CreditEntryRepository creditEntryRepository;

    private final DomainEventPublisher<PaymentCompletedEvent> paymentCompletedEventDomainEventPublisher;
    private final DomainEventPublisher<PaymentFailedEvent> paymentFailedEventDomainEventPublisher;
    private final DomainEventPublisher<PaymentCancelledEvent> paymentCancelledEventDomainEventPublisher;

    @Transactional
    public PaymentEvent persistPayment(PaymentRequest paymentRequest) {
        log.info("Received payment complete event for id : {}", paymentRequest.getOrderId());
        var payment = paymentDataMapper.paymentRequestModelToPayment(paymentRequest);
        var creditEntry = getCreditEntry(payment.getCustomerId());
        var creditHistory = getCreditHistory(payment.getCustomerId());
        List<String> failureMessage = new ArrayList<>();
        var paymentEvent = paymentDomainService.validateAndInitializePayment
                (payment, creditEntry, creditHistory, failureMessage, paymentCompletedEventDomainEventPublisher, paymentFailedEventDomainEventPublisher);
        persistDbObject(payment, creditEntry, creditHistory, failureMessage);
        return paymentEvent;
    }

    @Transactional
    public PaymentEvent persistCancelPayment(PaymentRequest paymentRequest) {
        log.info("Received payment cancel event for id : {}", paymentRequest.getOrderId());
        var payment = paymentRepository.findByOrderId
                (UUID.fromString(paymentRequest.getOrderId())).orElseThrow(
                () -> new PaymentNotFoundException("Payment not found"));
        var creditEntry = getCreditEntry(payment.getCustomerId());
        var creditHistory = getCreditHistory(payment.getCustomerId());
        List<String> failureMessage = new ArrayList<>();
        var paymentEvent = paymentDomainService.validateAndCancelledPayment
                (payment, creditEntry, creditHistory, failureMessage, paymentFailedEventDomainEventPublisher, paymentCancelledEventDomainEventPublisher);

        persistDbObject(payment, creditEntry, creditHistory, failureMessage);

        return paymentEvent;
    }

    private void persistDbObject(Payment payment,
                                 CreditEntry creditEntry,
                                 List<CreditHistory> creditHistory,
                                 List<String> failureMessage) {
        paymentRepository.save(payment);
        if (failureMessage.isEmpty()) {
            creditEntryRepository.save(creditEntry);
            creditHistoryRepository.save(creditHistory.get(creditHistory.size() - 1));
        }
    }

    private List<CreditHistory> getCreditHistory(CustomerId customerId) {
        return creditHistoryRepository.findByCustomerId(customerId).orElseThrow(
                () -> new PaymentApplicationServiceException
                        ("No credit history found for customer id : " + customerId));
    }

    private CreditEntry getCreditEntry(CustomerId customerId) {
        return creditEntryRepository.findByCustomerId(customerId).orElseThrow(
                () -> new PaymentApplicationServiceException
                        ("Credit entry not found for customer id : " + customerId));
    }
}
