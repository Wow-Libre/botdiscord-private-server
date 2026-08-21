package com.wow_libre.register_discord.application.register;

import com.wow_libre.register_discord.domain.exception.RateLimitException;
import com.wow_libre.register_discord.infrastructure.config.RegisterProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegistrationRateLimiterTest {

    private RegisterProperties properties;
    private MutableClock clock;
    private RegistrationRateLimiter limiter;

    @BeforeEach
    void setUp() {
        properties = new RegisterProperties();
        properties.setCooldownSeconds(300);
        properties.setFailedAttemptCooldownSeconds(30);
        properties.setMaxAttemptsPerHour(3);
        properties.setMaxAccountsPerUser(1);
        clock = new MutableClock(Instant.parse("2026-08-20T00:00:00Z"));
        limiter = new RegistrationRateLimiter(properties, clock);
    }

    @Test
    void allowsFirstAttempt() {
        assertDoesNotThrow(() -> limiter.assertCanStart("user-1"));
    }

    @Test
    void blocksAfterFailedAttemptCooldown() {
        limiter.recordAttempt("user-1");
        assertThrows(RateLimitException.class, () -> limiter.assertCanStart("user-1"));
        clock.advance(Duration.ofSeconds(31));
        assertDoesNotThrow(() -> limiter.assertCanStart("user-1"));
    }

    @Test
    void blocksAfterMaxAttemptsPerHour() {
        limiter.recordAttempt("user-1");
        clock.advance(Duration.ofSeconds(31));
        limiter.recordAttempt("user-1");
        clock.advance(Duration.ofSeconds(31));
        limiter.recordAttempt("user-1");
        clock.advance(Duration.ofSeconds(31));
        assertThrows(RateLimitException.class, () -> limiter.assertCanStart("user-1"));
    }

    @Test
    void blocksAfterSuccessfulAccount() {
        limiter.recordAttempt("user-1");
        limiter.recordSuccess("user-1");
        clock.advance(Duration.ofSeconds(400));
        assertThrows(RateLimitException.class, () -> limiter.assertCanStart("user-1"));
    }

    private static final class MutableClock extends Clock {
        private Instant instant;

        private MutableClock(Instant instant) {
            this.instant = instant;
        }

        void advance(Duration duration) {
            instant = instant.plus(duration);
        }

        @Override
        public ZoneOffset getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(java.time.ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return instant;
        }
    }
}
