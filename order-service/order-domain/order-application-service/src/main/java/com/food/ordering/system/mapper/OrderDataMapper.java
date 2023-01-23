package com.food.ordering.system.mapper;

import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.dto.create.CreateOrderCommand;
import com.food.ordering.system.dto.create.CreateOrderResponse;
import com.food.ordering.system.dto.create.OrderAddress;
import com.food.ordering.system.dto.track.TrackOrderResponse;
import com.food.ordering.system.entity.Order;
import com.food.ordering.system.entity.OrderItem;
import com.food.ordering.system.entity.Product;
import com.food.ordering.system.entity.Restaurant;
import com.food.ordering.system.event.OrderCreatedEvent;
import com.food.ordering.system.event.OrderPaidEvent;
import com.food.ordering.system.outbox.model.approval.OrderApprovalEventPayload;
import com.food.ordering.system.outbox.model.approval.OrderApprovalProduct;
import com.food.ordering.system.outbox.model.payment.OrderPaymentEventPayload;
import com.food.ordering.system.valueobject.StreetAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class OrderDataMapper {
    public Restaurant createOrderCommandToRestaurant(CreateOrderCommand createOrderCommand) {
        return Restaurant.builder()
                .id(new RestaurantId(createOrderCommand.restaurantId()))
                .products(createOrderCommand.items().stream()
                        .map(orderItem ->
                                new Product(new ProductId(orderItem.productId())))
                        .toList()
                )
                .build();
    }

    public Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand) {
        return Order.builder()
                .customerId(new CustomerId(createOrderCommand.customerId()))
                .restaurantId(new RestaurantId(createOrderCommand.restaurantId()))
                .deliveryAddress(orderAddressToStreetAddress(createOrderCommand.address()))
                .price(new Money(createOrderCommand.price()))
                .items(orderItemsToOrderItemEntities(createOrderCommand.items()))
                .build();
    }


    private List<OrderItem> orderItemsToOrderItemEntities(List<com.food.ordering.system.dto.create.OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem ->
                        OrderItem.builder()
                                .product(new Product(new ProductId(orderItem.productId())))
                                .price(new Money(orderItem.price()))
                                .quantity(orderItem.quantity())
                                .subTotal(new Money(orderItem.subTotal()))
                                .build())
                .toList();
    }

    private StreetAddress orderAddressToStreetAddress(OrderAddress orderAddress) {
        return new StreetAddress(
                UUID.randomUUID(),
                orderAddress.street(),
                orderAddress.city(),
                orderAddress.postalCode()
        );
    }

    public CreateOrderResponse orderToCreateOrderResponse(Order order, String message) {
        return CreateOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getStatus())
                .message(message)
                .build();
    }

    public TrackOrderResponse orderToTrackOrderResponse(Order order) {

        return TrackOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getStatus())
                .failureMessages(order.getFailureMessages())
                .build();
    }

    public OrderPaymentEventPayload orderCreatedEventToOrderPaymentEventPayload(OrderCreatedEvent order) {
        return OrderPaymentEventPayload.builder()
                .orderId(order.getOrder().getId().getValue().toString())
                .customerId(order.getOrder().getCustomerId().getValue().toString())
                .price(order.getOrder().getPrice().amount())
                .createdAt(order.getCreatedAt())
                .paymentOrderStatus(PaymentOrderStatus.PENDING.name())
                .build();
    }

    public OrderApprovalEventPayload orderPaidEventToOrderApprovalEventPayload(OrderPaidEvent orderPaidEvent) {
        return OrderApprovalEventPayload.builder()
                .orderId(orderPaidEvent.getOrder().getId().getValue().toString())
                .restaurantId(orderPaidEvent.getOrder().getRestaurantId().getValue().toString())
                .restaurantOrderStatus(RestaurantOrderStatus.PAID.name())
                .products(orderPaidEvent.getOrder().getItems().stream()
                        .map(orderItem -> OrderApprovalProduct.builder()
                                .id(orderItem.getProduct().getId().getValue().toString())
                                .quantity(orderItem.getQuantity())
                                .build())
                        .toList())
                .price(orderPaidEvent.getOrder().getPrice().amount())
                .createdAt(orderPaidEvent.getCreatedAt())
                .build();
    }
}
