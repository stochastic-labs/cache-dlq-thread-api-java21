package com.stochasticlabs.cachedlqthreadapijava21.application.query.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stochasticlabs.cachedlqthreadapijava21.application.query.dto.InputResponseDTO;
import com.stochasticlabs.cachedlqthreadapijava21.application.query.exceptions.ResourceNotFoundException;
import com.stochasticlabs.cachedlqthreadapijava21.infrastructure.db.mongo.repository.ProcessedInputRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class SearchInputUseCase {

    private static final Logger log = LoggerFactory.getLogger(SearchInputUseCase.class);
    private static final String CACHE_KEY_PREFIX = "input:cache:";

    private final ProcessedInputRepository processedInputRepository;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public SearchInputUseCase(ProcessedInputRepository processedInputRepository,
                              StringRedisTemplate redisTemplate,
                              ObjectMapper objectMapper) {
        this.processedInputRepository = processedInputRepository;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public InputResponseDTO execute(int originalValue) {
        String cacheKey = CACHE_KEY_PREFIX + originalValue;

        try {
            String cachedJson = redisTemplate.opsForValue().get(cacheKey);
            if (cachedJson != null) {
                log.info("[cache-aside] CACHE HIT for key: {}", cacheKey);
                return objectMapper.readValue(cachedJson, InputResponseDTO.class);
            }
        } catch (Exception e) {
            log.error("[cache-aside] Error Redis. Skip to database...", e);
        }

        log.info("[cache-aside] CACHE MISS for key: {}. Search in MongoDB...", cacheKey);
        var entity = processedInputRepository.findByOriginalValue(originalValue).stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No record found for the value: " + originalValue));

        InputResponseDTO response = new InputResponseDTO(entity.getOriginalValue(), entity.getStochasticValueGenerated());

        try {
            String jsonToCache = objectMapper.writeValueAsString(response);
            redisTemplate.opsForValue().set(cacheKey, jsonToCache, Duration.ofMinutes(5));
            log.info("[cache-aside] New data insert into Redis for key: {}", cacheKey);
        } catch (Exception e) {
            log.error("[cache-aside] Error Redis...", e);
        }

        return response;
    }
}
