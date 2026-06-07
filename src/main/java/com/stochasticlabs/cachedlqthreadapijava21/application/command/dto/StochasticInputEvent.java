package com.stochasticlabs.cachedlqthreadapijava21.application.command.dto;

public record StochasticInputEvent (
        int originalValue,
        int stochasticValueGenerated
) {}