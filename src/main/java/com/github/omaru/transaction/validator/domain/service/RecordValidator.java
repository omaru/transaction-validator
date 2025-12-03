package com.github.omaru.transaction.validator.domain.service;

import com.github.omaru.transaction.validator.domain.model.FailedRecord;
import com.github.omaru.transaction.validator.domain.model.Reason;
import com.github.omaru.transaction.validator.domain.model.RecordEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RecordValidator {
    public static final FailedRecord VALID_RECORD = null;

    public FailedRecord validate(RecordEntry record, Set<Long> seenTransactionReferences) {
        var balanceResult = validateEndBalance(record);
        var uniqueTransactionResult = validateUniqueTransactionReference(record, seenTransactionReferences);
        if (balanceResult != VALID_RECORD) {
            var reasons = new ArrayList<Reason>();
            reasons.addAll(balanceResult.getReasons());
            reasons.addAll(uniqueTransactionResult != VALID_RECORD ? uniqueTransactionResult.getReasons() : Collections.emptyList());
            return FailedRecord.builder()
                    .record(record)
                    .reasons(reasons)
                    .build();
        }
        return uniqueTransactionResult;
    }

    protected FailedRecord validateEndBalance(RecordEntry record) {
        BigDecimal calculated = record.getStartBalance()
                .add(record.getMutation())
                .setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal expected = record.getEndBalance()
                .setScale(2, RoundingMode.HALF_EVEN);
        return calculated.compareTo(expected) == 0 ? VALID_RECORD :
                FailedRecord.builder().record(record).reason(Reason.INCORRECT_END_BALANCE).build();
    }

    protected FailedRecord validateUniqueTransactionReference(RecordEntry record, Set<Long> seenTransactionReferences) {
        if (seenTransactionReferences.contains(record.getTransactionReference())) {
            return FailedRecord.builder().record(record).reason(Reason.DUPLICATE_REFERENCE).build();
        } else {
            seenTransactionReferences.add(record.getTransactionReference());
            return VALID_RECORD;
        }
    }

}
