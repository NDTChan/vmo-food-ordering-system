package com.food.ordering.system.data.access.restaurant.adapter;

import com.food.ordering.system.common.data.access.repository.RestaurantJpaRepository;
import com.food.ordering.system.data.access.restaurant.mapper.RestaurantDataAccessMapper;
import com.food.ordering.system.entity.Restaurant;
import com.food.ordering.system.ports.output.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

    @Override
    public Optional<Restaurant> findRestaurantInformation(Restaurant restaurant) {
        var restaurantProducts = restaurantDataAccessMapper.restaurantToRestaurantProducts(restaurant);
        var restaurantEntities = restaurantJpaRepository
                .findByRestaurantIdAndProductIdIn(restaurant.getId().getValue(), restaurantProducts);
        return restaurantEntities.map(restaurantDataAccessMapper::restaurantEntityToRestaurant);
    }
}
