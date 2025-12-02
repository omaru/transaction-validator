package com.github.omaru.transaction.validator.infrastructure.persistence.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "record_entry", uniqueConstraints = {
        @UniqueConstraint(columnNames = "transaction_reference")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordEntity {

    @Id
    @Builder.Default
    @Column(columnDefinition = "uuid", updatable = false, nullable = false, unique = true)
    private UUID id = UUID.randomUUID();

    @Column(name = "transaction_reference", unique = true, nullable = false)
    private Long transactionReference;

    @Column(name = "account_number", length = 40)
    private String accountNumber;

    @Column(name = "start_balance", precision = 15, scale = 2)
    private BigDecimal startBalance;

    @Column(name = "mutation", precision = 15, scale = 2)
    private BigDecimal mutation;

    @Column(name = "description", length = 50)
    private String description;

    @Column(name = "end_balance", precision = 15, scale = 2)
    private BigDecimal endBalance;
}