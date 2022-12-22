package com.food.ordering.system;

import com.food.ordering.system.dto.create.CreateOrderResponse;
import com.food.ordering.system.entity.Order;
import com.food.ordering.system.event.OrderCreatedEvent;
import com.food.ordering.system.mapper.OrderDataMapper;
import com.food.ordering.system.dto.create.CreateOrderCommand;
import com.food.ordering.system.entity.Customer;
import com.food.ordering.system.entity.Restaurant;
import com.food.ordering.system.exception.OrderDomainException;
import com.food.ordering.system.ports.output.repository.CustomerRepository;
import com.food.ordering.system.ports.output.repository.OrderRepository;
import com.food.ordering.system.ports.output.repository.RestaurantRepository;
import com.food.ordering.system.service.OrderDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreateCommandHandler {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderDataMapper orderDataMapper;

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.customerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order, restaurant);
        Order orderResult = saveOrder(order);
        return orderDataMapper.orderToCreateOrderResponse(orderResult, "Order created successfully");
    }

    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
        Restaurant restaurant = orderDataMapper.createOrderCommandToRestaurant(createOrderCommand);
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findRestaurantInformation(restaurant);
        if (optionalRestaurant.isEmpty()) {
            log.warn("Could not find restaurant with restaurant id: {}", createOrderCommand.restaurantId());
            throw new OrderDomainException("Could not find restaurant with restaurant id:" + createOrderCommand.restaurantId());
        }
        return optionalRestaurant.get();
    }

    private void checkCustomer(UUID customerId) {
        Optional<Customer> customerOptional = customerRepository.findCustomer(customerId);
        if (customerOptional.isEmpty()) {
            log.warn("Could not find customer with customer id: {}", customerId);
            throw new OrderDomainException("Could not find customer with customer id:" + customerId);
        }
    }

    private Order saveOrder(Order order) {
        Order orderResult = orderRepository.save(order);
        if (Objects.isNull(orderResult)) {
            log.error("Could not save order!");
            throw new OrderDomainException("Could not save order!");
        }
        log.info("Order is saved with id: {}", orderResult.getId().getValue());
        return orderResult;
    }
}
