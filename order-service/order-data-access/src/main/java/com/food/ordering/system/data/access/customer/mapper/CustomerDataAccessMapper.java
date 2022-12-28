package com.food.ordering.system.data.access.customer.mapper;

import com.food.ordering.system.data.access.customer.entity.CustomerEntity;
import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataAccessMapper {

    public Customer customerEntityToCustomer(CustomerEntity customerEntity) {
        return new Customer(new CustomerId(customerEntity.getId()));
    }

    public CustomerEntity customerToCustomerEntity(Customer customer) {
        return CustomerEntity.builder()
                .id(customer.getId().getValue())
                .build();
    }
}
