package com.food.ordering.system.data.access.customer.adapter;

import com.food.ordering.system.data.access.customer.mapper.CustomerDataAccessMapper;
import com.food.ordering.system.data.access.customer.repository.CustomerJPARepository;
import com.food.ordering.system.entity.Customer;
import com.food.ordering.system.ports.output.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {


    private final CustomerJPARepository customerJpaRepository;
    private final CustomerDataAccessMapper customerDataAccessMapper;


    @Override
    public Customer save(Customer customer) {
        return customerDataAccessMapper.customerEntityToCustomer(
                customerJpaRepository.save(customerDataAccessMapper.customerToCustomerEntity(customer)));
    }

    @Override
    public Optional<Customer> findCustomer(UUID customerId) {
        return customerJpaRepository.findById(customerId)
                .map(customerDataAccessMapper::customerEntityToCustomer);
    }
}
