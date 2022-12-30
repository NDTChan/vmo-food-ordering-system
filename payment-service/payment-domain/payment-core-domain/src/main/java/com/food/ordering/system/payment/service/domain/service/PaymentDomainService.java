package com.food.ordering.system.payment.service.domain.service;

import com.food.ordering.system.payment.service.domain.entity.CreditEntry;
import com.food.ordering.system.payment.service.domain.entity.CreditHistory;
import com.food.ordering.system.payment.service.domain.entity.Payment;
import com.food.ordering.system.payment.service.domain.event.PaymentEvent;

import java.util.List;

public interface PaymentDomainService {
    PaymentEvent validateAndInitializePayment(Payment paymentEvent,
                                              CreditEntry creditEntry,
                                              List<CreditHistory> creditHistory,
                                              List<String> failureMessages);

    PaymentEvent validateAndCancelledPayment(Payment paymentEvent,
                                             CreditEntry creditEntry,
                                             List<CreditHistory> creditHistory,
                                             List<String> failureMessages);
}
