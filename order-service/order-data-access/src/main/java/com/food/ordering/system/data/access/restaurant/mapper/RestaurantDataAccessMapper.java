package com.food.ordering.system.data.access.restaurant.mapper;

import com.food.ordering.system.data.access.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.data.access.restaurant.exception.RestaurantDataAccessException;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.ProductId;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.entity.Product;
import com.food.ordering.system.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class RestaurantDataAccessMapper {

    public List<UUID> restaurantToRestaurantProducts(Restaurant restaurant) {
        return restaurant.getProducts()
                .stream()
                .map(product -> product.getId().getValue())
                .toList();
    }

    public Restaurant restaurantEntityToRestaurant(List<RestaurantEntity> restaurantEntities) {
        var restaurantEntity =
                restaurantEntities.stream().findFirst()
                        .orElseThrow(() -> new RestaurantDataAccessException("No restaurant found"));
        var restaurantProducts = restaurantEntities.stream()
                .map(entity -> new Product(new ProductId(entity.getProductId()),
                        entity.getProductName(),
                        new Money(entity.getProductPrice()))).toList();
        return Restaurant.builder()
                .id(new RestaurantId(restaurantEntity.getRestaurantId()))
                .products(restaurantProducts)
                .isActive(restaurantEntity.getRestaurantActive())
                .build();

    }
}
