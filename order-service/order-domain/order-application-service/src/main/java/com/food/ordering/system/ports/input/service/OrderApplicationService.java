package com.food.ordering.system.ports.input.service;

import com.food.ordering.system.dto.create.CreateOrderResponse;
import com.food.ordering.system.dto.create.CreateOrderCommand;
import com.food.ordering.system.dto.track.TrackOrderQuery;
import com.food.ordering.system.dto.track.TrackOrderResponse;

import javax.validation.Valid;

public interface OrderApplicationService {
    CreateOrderResponse createOrder(@Valid CreateOrderCommand createOrderCommand);

    TrackOrderResponse trackOrder(@Valid TrackOrderQuery trackOrderQuery);
}
