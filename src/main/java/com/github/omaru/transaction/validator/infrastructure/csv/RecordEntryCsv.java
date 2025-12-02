package com.github.omaru.transaction.validator.infrastructure.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RecordEntryCsv {
    @CsvBindByName(column = "Reference")
    private Long reference;
    @CsvBindByName(column = "AccountNumber")
    private String accountNumber;
    @CsvBindByName(column = "Description")
    private String description;
    @CsvBindByName(column = "Start Balance")
    private BigDecimal startBalance;
    @CsvBindByName(column = "Mutation")
    private BigDecimal mutation;
    @CsvBindByName(column = "End Balance")
    private BigDecimal endBalance;

}
