package com.github.omaru.transaction.validator.infrastructure.persistence.jpa;

import com.github.omaru.transaction.validator.domain.model.RecordEntry;
import com.github.omaru.transaction.validator.domain.service.RecordService;
import com.github.omaru.transaction.validator.infrastructure.persistence.jpa.mappper.RecordEntryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecordJpaService implements RecordService {
    private final RecordRepository recordRepository;
    private final RecordEntryMapper mapper;
    private static final String UNIQUE_CONSTRAINT_VIOLATION_MESSAGE = "violates unique constraint \"record_entry_transaction_reference_key\"";

    @Override
    public RecordEntry save(RecordEntry record) throws NotUniqueReferenceNumberException {
        try {
            var savedEntity = recordRepository.save(mapper.toEntity(record));
            return mapper.toDomain(savedEntity);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage() != null && e.getMessage().contains(UNIQUE_CONSTRAINT_VIOLATION_MESSAGE)) {
                throw new NotUniqueReferenceNumberException(e, record);
            } else {
                throw e;
            }
        }
    }

    @Override
    public void deleteAll() {
        recordRepository.deleteAll();
    }
}
