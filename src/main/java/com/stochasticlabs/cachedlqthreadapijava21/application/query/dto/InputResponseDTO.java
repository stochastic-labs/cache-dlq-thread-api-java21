package com.stochasticlabs.cachedlqthreadapijava21.application.query.dto;

import java.io.Serializable;

public record InputResponseDTO(
        int originalValue,
        int stochasticValueGenerated
) implements Serializable {}
