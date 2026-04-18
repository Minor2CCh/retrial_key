package com.Minor2CCh.retrial_key.config;

public class ModConfig {
    private Integer heavyRetrialKeyDurability = 256;
    private Double unstableEventProbably = 0.2;
    private Long skipCooldownTime = 0L;
    public void fillDefaults() {
        if (heavyRetrialKeyDurability == null)
            heavyRetrialKeyDurability = 256;
        if (unstableEventProbably == null)
            unstableEventProbably = 0.2;
        if (skipCooldownTime == null)
            skipCooldownTime = 0L;
    }
    public int getHeavyRetrialKeyDurability() {
        return Math.max(heavyRetrialKeyDurability, 1);
    }
    public double getUnstableEventProbably() {
        return Math.clamp(unstableEventProbably, 0, 1);
    }
    public Long getSkipCooldownTime() {
        return Math.max(skipCooldownTime, 0L);
    }
}
