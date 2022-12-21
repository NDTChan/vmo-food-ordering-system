package com.food.ordering.system.dto.create;

import com.food.ordering.system.order.service.domain.entity.OrderItem;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
public record CreateOrderCommand(@NotNull UUID customerId, @NotNull UUID restaurantId, @NotNull BigDecimal price,
                                 @NotNull List<OrderItem> orderItems, @NotNull OrderAddress orderAddress) {
}
