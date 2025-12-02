package com.github.omaru.transaction.validator.domain.service;

import com.github.omaru.transaction.validator.domain.model.FailedRecord;
import com.github.omaru.transaction.validator.domain.model.Reason;
import com.github.omaru.transaction.validator.domain.model.RecordEntry;
import com.github.omaru.transaction.validator.infrastructure.persistence.jpa.NotUniqueReferenceNumberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecordValidator {
    public static final FailedRecord VALID_RECORD = null;
    private final RecordService recordService;

    public FailedRecord validateEndBalance(RecordEntry record) {
        return record.getStartBalance()
                .add(record.getMutation()).equals(record.getEndBalance()) ? VALID_RECORD :
                FailedRecord.builder().record(record).reason(Reason.INCORRECT_END_BALANCE).build();
    }

    public FailedRecord validateUniqueTransactionReference(RecordEntry record) {
        try {
            recordService.save(record);
            return VALID_RECORD;
        } catch (NotUniqueReferenceNumberException e) {
            return FailedRecord.builder().record(record).reason(Reason.DUPLICATE_REFERENCE).build();
        }
    }

}
