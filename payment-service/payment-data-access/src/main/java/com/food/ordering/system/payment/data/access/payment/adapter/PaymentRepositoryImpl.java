package com.food.ordering.system.payment.data.access.payment.adapter;


import com.food.ordering.system.payment.application.service.ports.output.repository.PaymentRepository;
import com.food.ordering.system.payment.data.access.payment.mapper.PaymentDataAccessMapper;
import com.food.ordering.system.payment.data.access.payment.repository.PaymentJpaRepository;
import com.food.ordering.system.payment.service.domain.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;
    private final PaymentDataAccessMapper paymentDataAccessMapper;

    @Override
    public Payment save(Payment payment) {
        return paymentDataAccessMapper
                .paymentEntityToPayment(paymentJpaRepository
                        .save(paymentDataAccessMapper.paymentToPaymentEntity(payment)));
    }

    @Override
    public Optional<Payment> findByOrderId(UUID orderId) {
        return paymentJpaRepository.findByOrderId(orderId)
                .map(paymentDataAccessMapper::paymentEntityToPayment);
    }
}
