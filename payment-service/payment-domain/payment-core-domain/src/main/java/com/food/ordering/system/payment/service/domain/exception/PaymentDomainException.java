package com.food.ordering.system.payment.service.domain.exception;

public class PaymentDomainException extends RuntimeException {

    public PaymentDomainException(String message) {
        super(message);
    }

    public PaymentDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
