package com.food.ordering.system.order.service.messaging.mapper;

import com.food.ordering.system.domain.valueobject.OrderApprovalStatus;
import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.dto.message.PaymentResponse;
import com.food.ordering.system.dto.message.RestaurantApprovalResponse;
import com.food.ordering.system.entity.Order;
import com.food.ordering.system.event.OrderCancelledEvent;
import com.food.ordering.system.event.OrderCreatedEvent;
import com.food.ordering.system.event.OrderPaidEvent;
import com.food.ordering.system.kafka.order.avro.model.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderMessagingDataMapper {
    public PaymentRequestAvroModel orderCreatedEventToPaymentRequestAvroModel(OrderCreatedEvent orderCreatedEvent) {
        Order order = orderCreatedEvent.getOrder();
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setCustomerId(order.getCustomerId().getValue().toString())
                .setOrderId(order.getId().getValue().toString())
                .setPrice(order.getPrice().amount())
                .setCreatedAt(orderCreatedEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.PENDING)
                .build();
    }

    public PaymentRequestAvroModel orderCancelledEventToPaymentRequestAvroModel(OrderCancelledEvent orderCancelledEvent) {
        Order order = orderCancelledEvent.getOrder();
        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setCustomerId(order.getCustomerId().getValue().toString())
                .setOrderId(order.getId().getValue().toString())
                .setPrice(order.getPrice().amount())
                .setCreatedAt(orderCancelledEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.PENDING)
                .build();
    }

    public RestaurantApprovalRequestAvroModel
    orderPaidEventToRestaurantApprovalRequestAvroModel(OrderPaidEvent orderPaidEvent) {
        Order order = orderPaidEvent.getOrder();
        return RestaurantApprovalRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setOrderId(order.getId().getValue().toString())
                .setRestaurantId(order.getRestaurantId().getValue().toString())
//                .setRestaurantOrderStatus(RestaurantOrderStatus
//                        .valueOf(order.getStatus().name()))
                .setProducts(order.getItems().stream().map(orderItem ->
                        Product.newBuilder()
                                .setId(orderItem.getProduct().getId().getValue().toString())
                                .setQuantity(orderItem.getQuantity())
                                .build()).toList())
                .setPrice(order.getPrice().amount())
                .setCreatedAt(orderPaidEvent.getCreatedAt().toInstant())
                .build();
    }

    public PaymentResponse paymentResponseAvroModelToPaymentResponse(PaymentResponseAvroModel message) {
        return PaymentResponse.builder()
                .id(message.getId())
                .orderId(message.getOrderId())
                .sagaId(message.getSagaId())
                .paymentId(message.getPaymentId())
                .customerId(message.getCustomerId())
                .orderId(message.getOrderId())
                .price(message.getPrice())
                .createdAt(message.getCreatedAt())
                .paymentStatus(PaymentStatus.valueOf(message.getPaymentStatus().name()))
                .failureMessages(message.getFailureMessages())
                .build();
    }

    public RestaurantApprovalResponse restaurantApprovalResponseAvroModelToRestaurantApprovalResponse(RestaurantApprovalResponseAvroModel message) {
        return RestaurantApprovalResponse.builder()
                .orderId(message.getOrderId())
                .sagaId(message.getSagaId())
                .restaurantId(message.getRestaurantId())
                .id(message.getId())
                .sagaId(message.getSagaId())
                .createdAt(message.getCreatedAt())
                .orderApprovalStatus(OrderApprovalStatus.valueOf(message.getOrderApprovalStatus().name()))
                .failureMessages(message.getFailureMessages())
                .build();
    }
}
