package com.food.ordering.system.ports.output.repository;

import com.food.ordering.system.entity.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    Customer save(Customer customer);

    Optional<Customer> findCustomer(UUID customerId);
}
