package com.Minor2CCh.retrial_key.config;

public class ModConfig {
    public Integer heavyRetrialKeyDurability = 256;
    public Double unstableEventProbably = 0.2;
    public Long skipCooldownTime = 0L;
    public void fillDefaults() {
        if (heavyRetrialKeyDurability == null)
            heavyRetrialKeyDurability = 256;
        if (unstableEventProbably == null)
            unstableEventProbably = 0.2;
        if (skipCooldownTime == null)
            skipCooldownTime = 0L;
    }
}
