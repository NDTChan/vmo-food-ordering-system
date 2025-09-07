package com.food.ordering.system.outbox.scheduler.approval;


import com.food.ordering.system.exception.OrderDomainException;
import com.food.ordering.system.outbox.OutboxScheduler;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.ordering.system.saga.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantApprovalOutboxCleanerSchedule implements OutboxScheduler {

    private final ApprovalOutboxHelper approvalOutboxHelper;

    @Override
    @Scheduled(cron = "@midnight")
    public void processOutboxMessage() {

        var outboxMessageResponse =
                approvalOutboxHelper.getApprovalOutboxMessageByOutboxStatusAndSagaStatus(
                                OutboxStatus.COMPLETED,
                                SagaStatus.SUCCEEDED,
                                SagaStatus.FAILED,
                                SagaStatus.COMPENSATING).
                        orElseThrow(() -> new OrderDomainException("No outbox message found for processing"));

        if (Objects.nonNull(outboxMessageResponse)) {

            log.info("Received {} OrderPaymentOutboxMessage for clean-up. The Payloads :{}",
                    outboxMessageResponse.size(),
                    outboxMessageResponse.stream().map(OrderApprovalOutboxMessage::getPayload)
                            .collect(Collectors.joining(",")));
            approvalOutboxHelper.deleteApprovalOutboxMessageByOutboxStatusAndSagaStatus(
                    OutboxStatus.COMPLETED,
                    SagaStatus.SUCCEEDED,
                    SagaStatus.FAILED,
                    SagaStatus.COMPENSATING);
            log.info("Clean-up completed ! DELETED LOG SIZE : {}", outboxMessageResponse.size());

        }

    }
}
