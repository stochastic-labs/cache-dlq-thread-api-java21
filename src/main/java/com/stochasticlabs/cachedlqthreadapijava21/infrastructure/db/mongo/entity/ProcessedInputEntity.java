package com.stochasticlabs.cachedlqthreadapijava21.infrastructure.db.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "processed_inputs")
public class ProcessedInputEntity {

    @Id
    private String id;

    @Indexed(unique = true)
    private int originalValue;

    private int stochasticValueGenerated;

    private LocalDateTime ingestedAt;

    public ProcessedInputEntity() {}

    public ProcessedInputEntity(int originalValue, int stochasticValueGenerated) {
        this.originalValue = originalValue;
        this.stochasticValueGenerated = stochasticValueGenerated;
        this.ingestedAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public int getOriginalValue() { return originalValue; }
    public int getStochasticValueGenerated() { return stochasticValueGenerated; }
    public LocalDateTime getIngestedAt() { return ingestedAt; }
}
