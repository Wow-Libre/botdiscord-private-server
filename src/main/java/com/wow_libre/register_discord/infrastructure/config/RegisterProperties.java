package com.wow_libre.register_discord.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "register")
public class RegisterProperties {

    private int cooldownSeconds = 300;
    private int failedAttemptCooldownSeconds = 30;
    private int maxAttemptsPerHour = 3;
    private int maxAccountsPerUser = 1;

    public int getCooldownSeconds() {
        return cooldownSeconds;
    }

    public void setCooldownSeconds(int cooldownSeconds) {
        this.cooldownSeconds = cooldownSeconds;
    }

    public int getFailedAttemptCooldownSeconds() {
        return failedAttemptCooldownSeconds;
    }

    public void setFailedAttemptCooldownSeconds(int failedAttemptCooldownSeconds) {
        this.failedAttemptCooldownSeconds = failedAttemptCooldownSeconds;
    }

    public int getMaxAttemptsPerHour() {
        return maxAttemptsPerHour;
    }

    public void setMaxAttemptsPerHour(int maxAttemptsPerHour) {
        this.maxAttemptsPerHour = maxAttemptsPerHour;
    }

    public int getMaxAccountsPerUser() {
        return maxAccountsPerUser;
    }

    public void setMaxAccountsPerUser(int maxAccountsPerUser) {
        this.maxAccountsPerUser = maxAccountsPerUser;
    }
}
