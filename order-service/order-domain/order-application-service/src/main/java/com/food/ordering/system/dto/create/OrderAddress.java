package com.food.ordering.system.dto.create;

import lombok.Builder;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Builder
public record OrderAddress(@NotNull @Max(value = 50) String street,
                           @NotNull @Max(value = 50) String city,
                           @NotNull @Max(value = 10) String postalCode) {
}
