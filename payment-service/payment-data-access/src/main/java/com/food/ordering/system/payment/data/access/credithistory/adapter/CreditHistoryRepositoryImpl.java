package com.food.ordering.system.payment.data.access.credithistory.adapter;


import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.payment.application.service.ports.output.repository.CreditHistoryRepository;
import com.food.ordering.system.payment.data.access.credithistory.entity.CreditHistoryEntity;
import com.food.ordering.system.payment.data.access.credithistory.mapper.CreditHistoryDataAccessMapper;
import com.food.ordering.system.payment.data.access.credithistory.repository.CreditHistoryJpaRepository;
import com.food.ordering.system.payment.service.domain.entity.CreditHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CreditHistoryRepositoryImpl implements CreditHistoryRepository {

    private final CreditHistoryJpaRepository creditHistoryJpaRepository;
    private final CreditHistoryDataAccessMapper creditHistoryDataAccessMapper;

    @Override
    public CreditHistory save(CreditHistory creditHistory) {
        return creditHistoryDataAccessMapper.creditHistoryEntityToCreditHistory(creditHistoryJpaRepository
                .save(creditHistoryDataAccessMapper.creditHistoryToCreditHistoryEntity(creditHistory)));
    }


    @Override
    public Optional<List<CreditHistory>> findByCustomerId(CustomerId customerId) {
        Optional<List<CreditHistoryEntity>> creditHistory =
                creditHistoryJpaRepository.findByCustomerId(customerId.getValue());
        return creditHistory
                .map(creditHistoryList ->
                        creditHistoryList.stream()
                                .map(creditHistoryDataAccessMapper::creditHistoryEntityToCreditHistory)
                                .collect(Collectors.toList()));
    }
}
