package com.github.omaru.transaction.validator.application.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class RecordResponse {
    private UUID id;
    private Long transactionReference;
    private String accountNumber;
    private BigDecimal startBalance;
    private BigDecimal mutation;
    private String description;
    private BigDecimal endBalance;
}
