package com.stochasticlabs.cachedlqthreadapijava21.infrastructure.db.mongo.repository;

import com.stochasticlabs.cachedlqthreadapijava21.infrastructure.db.mongo.entity.ProcessedInputEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProcessedInputRepository extends MongoRepository<ProcessedInputEntity, String> {
    Optional<ProcessedInputEntity> findByOriginalValue(int originalValue);
}
