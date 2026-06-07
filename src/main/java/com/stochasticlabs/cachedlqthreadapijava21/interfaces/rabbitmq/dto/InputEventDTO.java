package com.stochasticlabs.cachedlqthreadapijava21.interfaces.rabbitmq.dto;

public record InputEventDTO(
        int originalValue,
        int stochasticValueGenerated
) {}