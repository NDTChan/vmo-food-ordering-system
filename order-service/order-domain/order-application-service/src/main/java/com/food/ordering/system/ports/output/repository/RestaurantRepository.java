package com.food.ordering.system.ports.output.repository;

import com.food.ordering.system.entity.Restaurant;

import java.util.Optional;

public interface RestaurantRepository {
    Optional<Restaurant> findRestaurantInformation(Restaurant restaurant);
}
