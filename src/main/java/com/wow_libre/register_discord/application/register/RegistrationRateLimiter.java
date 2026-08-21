package com.wow_libre.register_discord.application.register;

import com.wow_libre.register_discord.domain.exception.RateLimitException;
import com.wow_libre.register_discord.infrastructure.config.RegisterProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RegistrationRateLimiter {

    private final RegisterProperties properties;
    private final Clock clock;
    private final Map<String, UserWindow> windows = new ConcurrentHashMap<>();

    @Autowired
    public RegistrationRateLimiter(RegisterProperties properties, ObjectProvider<Clock> clock) {
        this.properties = properties;
        this.clock = clock.getIfAvailable(Clock::systemUTC);
    }

    RegistrationRateLimiter(RegisterProperties properties, Clock clock) {
        this.properties = properties;
        this.clock = clock;
    }

    public void assertCanStart(String discordUserId) {
        UserWindow window = windows.computeIfAbsent(discordUserId, ignored -> new UserWindow());
        synchronized (window) {
            Instant now = clock.instant();
            pruneAttempts(window, now);

            if (window.successfulAccounts >= properties.getMaxAccountsPerUser()) {
                throw new RateLimitException(
                        "Ya alcanzaste el máximo de cuentas permitidas desde Discord.",
                        Duration.ofDays(1));
            }
            Instant blockedUntil = window.blockedUntil;
            if (blockedUntil != null && now.isBefore(blockedUntil)) {
                throw new RateLimitException(
                        "Espera un momento antes de volver a registrarte.",
                        Duration.between(now, blockedUntil));
            }
            if (window.attempts.size() >= properties.getMaxAttemptsPerHour()) {
                Instant retryAt = window.attempts.peekFirst().plus(Duration.ofHours(1));
                throw new RateLimitException(
                        "Demasiados intentos de registro. Inténtalo más tarde.",
                        Duration.between(now, retryAt));
            }
        }
    }

    public void recordAttempt(String discordUserId) {
        UserWindow window = windows.computeIfAbsent(discordUserId, ignored -> new UserWindow());
        synchronized (window) {
            Instant now = clock.instant();
            pruneAttempts(window, now);
            window.attempts.addLast(now);
            window.blockedUntil = now.plusSeconds(properties.getFailedAttemptCooldownSeconds());
        }
    }

    public void recordSuccess(String discordUserId) {
        UserWindow window = windows.computeIfAbsent(discordUserId, ignored -> new UserWindow());
        synchronized (window) {
            Instant now = clock.instant();
            window.successfulAccounts++;
            window.blockedUntil = now.plusSeconds(properties.getCooldownSeconds());
        }
    }

    private void pruneAttempts(UserWindow window, Instant now) {
        Instant cutoff = now.minus(Duration.ofHours(1));
        while (!window.attempts.isEmpty() && window.attempts.peekFirst().isBefore(cutoff)) {
            window.attempts.removeFirst();
        }
    }

    private static final class UserWindow {
        private final Deque<Instant> attempts = new ArrayDeque<>();
        private Instant blockedUntil;
        private int successfulAccounts;
    }
}
