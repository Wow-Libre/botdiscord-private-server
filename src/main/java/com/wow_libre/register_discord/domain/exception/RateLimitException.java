package com.wow_libre.register_discord.domain.exception;

import java.time.Duration;

public class RateLimitException extends RegisterException {

    private final Duration retryAfter;

    public RateLimitException(String message, Duration retryAfter) {
        super(message);
        this.retryAfter = retryAfter;
    }

    public Duration getRetryAfter() {
        return retryAfter;
    }
}
