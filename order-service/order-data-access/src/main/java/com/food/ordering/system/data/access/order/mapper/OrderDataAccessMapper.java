package com.food.ordering.system.data.access.order.mapper;

import com.food.ordering.system.data.access.order.entity.OrderAddressEntity;
import com.food.ordering.system.data.access.order.entity.OrderEntity;
import com.food.ordering.system.data.access.order.entity.OrderItemEntity;
import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.entity.Order;
import com.food.ordering.system.entity.OrderItem;
import com.food.ordering.system.entity.Product;
import com.food.ordering.system.valueobject.OrderItemId;
import com.food.ordering.system.valueobject.StreetAddress;
import com.food.ordering.system.valueobject.TrackingId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.food.ordering.system.entity.Order.FAILURE_MESSAGE_DELIMITER;

@Component
public class OrderDataAccessMapper {
    public OrderEntity orderToOrderEntity(Order order) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(order.getId().getValue());
        orderEntity.setOrderStatus(order.getStatus());
        orderEntity.setCustomerId(order.getCustomerId().getValue());
        orderEntity.setRestaurantId(order.getRestaurantId().getValue());
        orderEntity.setTrackingId(order.getTrackingId().getValue());
        orderEntity.setAddress(deliveryAddressToAddressEntity(order.getDeliveryAddress()));
        orderEntity.setPrice(order.getPrice().amount());
        orderEntity.setItems(orderItemsToOrderItemsEntity(order.getItems()));
        orderEntity.setFailureMessages(Objects.nonNull(order.getFailureMessages()) ?
                String.join(FAILURE_MESSAGE_DELIMITER, order.getFailureMessages()) : "");
        orderEntity.getAddress().setOrder(orderEntity);
        orderEntity.getItems().forEach(item -> item.setOrderEntity(orderEntity));
        return orderEntity;
    }

    public Order orderEntityToOrder(OrderEntity orderEntity) {
        return Order.builder()
                .orderId(new OrderId(orderEntity.getId()))
                .customerId(new CustomerId(orderEntity.getCustomerId()))
                .restaurantId(new RestaurantId(orderEntity.getRestaurantId()))
                .deliveryAddress(addressEntityToDeliveryAddress(orderEntity.getAddress()))
                .price(new Money(orderEntity.getPrice()))
                .items(orderItemsEntityToOrderItems(orderEntity.getItems()))
                .trackingId(new TrackingId(orderEntity.getTrackingId()))
                .status(orderEntity.getOrderStatus())
                .failureMessages(Objects.isNull(orderEntity.getFailureMessages()) ? new ArrayList<>() :
                        new ArrayList<>(Arrays.asList(orderEntity.getFailureMessages()
                                .split(FAILURE_MESSAGE_DELIMITER))))
                .build();
    }

    private List<OrderItem> orderItemsEntityToOrderItems(List<OrderItemEntity> items) {
        return items.stream()
                .map(item -> OrderItem.builder()
                        .orderItemId(new OrderItemId(item.getId()))
                        .product(new Product(new ProductId(item.getProductId())))
                        .quantity(item.getQuantity())
                        .price(new Money(item.getPrice()))
                        .subTotal(new Money(item.getSubTotal()))
                        .build())
                .toList();
    }

    private StreetAddress addressEntityToDeliveryAddress(OrderAddressEntity address) {
        return new StreetAddress(address.getId(), address.getStreet(), address.getCity(), address.getPostalCode());
    }

    private List<OrderItemEntity> orderItemsToOrderItemsEntity(List<OrderItem> items) {
        return items.stream()
                .map(item -> OrderItemEntity.builder()
                        .id(item.getId().getValue())
                        .productId(item.getProduct().getId().getValue())
                        .price(item.getPrice().amount())
                        .quantity(item.getQuantity())
                        .subTotal(item.getSubTotal().amount())
                        .build())
                .toList();
    }

    private OrderAddressEntity deliveryAddressToAddressEntity(StreetAddress deliveryAddress) {
        OrderAddressEntity orderAddressEntity = new OrderAddressEntity();
        orderAddressEntity.setId(deliveryAddress.getId());
        orderAddressEntity.setStreet(deliveryAddress.getStreet());
        orderAddressEntity.setCity(deliveryAddress.getCity());
        orderAddressEntity.setPostalCode(deliveryAddress.getPostalCode());
        return orderAddressEntity;
    }
}
