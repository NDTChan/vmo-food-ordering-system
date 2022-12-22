package com.food.ordering.system.mapper;

import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.ProductId;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.dto.create.CreateOrderCommand;
import com.food.ordering.system.dto.create.CreateOrderResponse;
import com.food.ordering.system.dto.create.OrderAddress;
import com.food.ordering.system.entity.Order;
import com.food.ordering.system.entity.OrderItem;
import com.food.ordering.system.entity.Product;
import com.food.ordering.system.entity.Restaurant;
import com.food.ordering.system.valueobject.StreetAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class OrderDataMapper {
    public Restaurant createOrderCommandToRestaurant(CreateOrderCommand createOrderCommand) {
        return Restaurant.builder()
                .id(new RestaurantId(createOrderCommand.restaurantId()))
                .products(createOrderCommand.orderItems().stream()
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
                .deliveryAddress(orderAddressToStreetAddress(createOrderCommand.orderAddress()))
                .price(new Money(createOrderCommand.price()))
                .items(orderItemsToOrderItemEntities(createOrderCommand.orderItems()))
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

}
