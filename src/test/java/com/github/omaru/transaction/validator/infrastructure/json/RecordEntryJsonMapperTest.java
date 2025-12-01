package com.github.omaru.transaction.validator.infrastructure.json;

import com.github.omaru.transaction.validator.infrastructure.csv.RecordEntryCsvMapper;
import org.junit.jupiter.api.BeforeEach;

class RecordEntryJsonMapperTest {
    private RecordEntryCsvMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new RecordEntryCsvMapper();
    }
}
