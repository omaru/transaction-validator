package com.github.omaru.transaction.validator.infrastructure.persistence.jpa;

import com.github.omaru.transaction.validator.domain.model.RecordEntry;
import lombok.Getter;

import java.text.MessageFormat;

@Getter
public class NotUniqueReferenceNumberException extends Throwable {
    private static final String MESSAGE = "Reference number {0} is not unique";
    private final RecordEntry recordEntry;

    public NotUniqueReferenceNumberException(Throwable t, RecordEntry recordEntry) {
        super(MessageFormat.format(MESSAGE, String.valueOf(recordEntry.getTransactionReference())), t);
        this.recordEntry = recordEntry;
    }
}
