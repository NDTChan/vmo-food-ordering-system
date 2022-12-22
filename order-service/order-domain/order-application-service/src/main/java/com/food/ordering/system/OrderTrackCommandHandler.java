package com.food.ordering.system;

import com.food.ordering.system.dto.track.TrackOrderQuery;
import com.food.ordering.system.dto.track.TrackOrderResponse;
import com.food.ordering.system.exception.OrderNotFoundException;
import com.food.ordering.system.mapper.OrderDataMapper;
import com.food.ordering.system.ports.output.repository.OrderRepository;
import com.food.ordering.system.valueobject.TrackingId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderTrackCommandHandler {

    private final OrderRepository orderRepository;
    private final OrderDataMapper orderDataMapper;


    @Transactional(readOnly = true)
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        var order = orderRepository.findByTrackingId
                        (new TrackingId(trackOrderQuery.orderTrackingId()))
                .orElseThrow(() -> new OrderNotFoundException
                        ("Order not found with tracking id : +  " + trackOrderQuery.orderTrackingId()));
        return orderDataMapper.orderToTrackOrderResponse(order);
    }
}
