package com.github.omaru.transaction.validator.infrastructure.json;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RecordEntryJson {
    private Long reference;
    private String accountNumber;
    private String description;
    private BigDecimal startBalance;
    private BigDecimal mutation;
    private BigDecimal endBalance;
}
