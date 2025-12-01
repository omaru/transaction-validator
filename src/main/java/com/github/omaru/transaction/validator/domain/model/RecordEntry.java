package com.github.omaru.transaction.validator.domain.model;

import lombok.Builder;
import lombok.Data;
import org.iban4j.Iban;

import java.math.BigDecimal;

@Data
@Builder
public class RecordEntry {
    private Long transactionReference;
    private Iban accountNumber;
    private BigDecimal startBalance;
    private BigDecimal mutation;
    private String description;
    private BigDecimal endBalance;
}
