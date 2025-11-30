package com.github.omaru.transaction.validator.domain.model;

import lombok.Data;
import org.iban4j.Iban;

import java.math.BigDecimal;

@Data
public class Record {
    private Long transactionReference;
    private Iban accountNumber;
    private BigDecimal startBalance;
    private BigDecimal mutation;
    private String description;
    private BigDecimal endBalance;
}
