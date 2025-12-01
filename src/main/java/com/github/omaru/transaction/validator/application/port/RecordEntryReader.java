package com.github.omaru.transaction.validator.application.port;

import com.github.omaru.transaction.validator.domain.model.RecordEntry;

import java.util.stream.Stream;

public interface RecordEntryReader<T> {
    Stream<RecordEntry> read(T t);
}
