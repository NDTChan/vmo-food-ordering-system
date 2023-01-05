package com.food.ordering.system.saga;

import com.food.ordering.system.domain.event.DomainEvent;

public interface SagaStep<T, U extends DomainEvent, S extends DomainEvent> {
    U process(T data);

    S rollback(T data);
}
