package com.food.ordering.system.outbox.model.approval;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderApprovalProduct {

    @JsonProperty
    private String id;
    @JsonProperty
    private Integer quantity;

}
