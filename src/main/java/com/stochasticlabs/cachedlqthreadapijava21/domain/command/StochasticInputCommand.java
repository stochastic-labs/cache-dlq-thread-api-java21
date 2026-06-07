package com.stochasticlabs.cachedlqthreadapijava21.domain.command;

public record StochasticInputCommand(
        int integer,
        int stochasticValue
) {
    public StochasticInputCommand {
        if (integer < 0) {
            throw new IllegalArgumentException("The original value cannot be negative");
        }
    }
}
