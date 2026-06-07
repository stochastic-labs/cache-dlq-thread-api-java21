package com.stochasticlabs.cachedlqthreadapijava21.application.command.usecase;

import com.stochasticlabs.cachedlqthreadapijava21.application.command.dto.StochasticInputEvent;
import com.stochasticlabs.cachedlqthreadapijava21.domain.command.StochasticInputCommand;
import com.stochasticlabs.cachedlqthreadapijava21.infrastructure.db.mongo.entity.ProcessedInputEntity;
import com.stochasticlabs.cachedlqthreadapijava21.infrastructure.db.mongo.repository.ProcessedInputRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class IngestStochasticInputUseCase {

    private static final Logger log = LoggerFactory.getLogger(IngestStochasticInputUseCase.class);
    private static final String LOCK_PREFIX = "lock:input:";

    private final ProcessedInputRepository processedInputRepository;
    private final StringRedisTemplate redisTemplate;

    public IngestStochasticInputUseCase(ProcessedInputRepository processedInputRepository,
                                        StringRedisTemplate redisTemplate) {
        this.processedInputRepository = processedInputRepository;
        this.redisTemplate = redisTemplate;
    }

    public void execute(StochasticInputEvent event) {
        String idempotencyKey = LOCK_PREFIX + event.originalValue();

        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(idempotencyKey, "PROCESSING", Duration.ofHours(24));

        if (Boolean.FALSE.equals(success)) {
            log.warn("[idempotency-redis] Duplicated data: {}. Skip process.", event.originalValue());
            return;
        }

        try {
            StochasticInputCommand command = new StochasticInputCommand(
                    event.originalValue(),
                    event.stochasticValueGenerated()
            );

            ProcessedInputEntity entity = new ProcessedInputEntity(
                    command.integer(),
                    command.stochasticValue()
            );

            processedInputRepository.insert(entity);
            log.info("[use-case] Data persist success MongoDB: {}", command.integer());

        } catch (Exception e) {
            redisTemplate.delete(idempotencyKey);
            log.error("[use-case] Error in process: {}. Free idempotent key.", event.originalValue());
            throw e;
        }
    }
}
