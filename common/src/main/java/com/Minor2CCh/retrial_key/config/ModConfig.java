package com.Minor2CCh.retrial_key.config;

import net.minecraft.util.math.MathHelper;

public class ModConfig {
    private Integer heavyRetrialKeyDurability = 256;
    private Double unstableEventProbably = 0.2;
    private Long skipCooldownTime = 0L;
    private Boolean tradableKeyWayMold = false;
    public void fillDefaults() {
        if (heavyRetrialKeyDurability == null)
            heavyRetrialKeyDurability = 256;
        if (unstableEventProbably == null)
            unstableEventProbably = 0.2;
        if (skipCooldownTime == null)
            skipCooldownTime = 0L;
        if (tradableKeyWayMold == null)
            tradableKeyWayMold = false;
    }
    public int getHeavyRetrialKeyDurability() {
        return Math.max(heavyRetrialKeyDurability, 1);
    }
    public double getUnstableEventProbably() {
        return MathHelper.clamp(unstableEventProbably, 0, 1);
    }
    public Long getSkipCooldownTime() {
        return Math.max(skipCooldownTime, 0L);
    }
    public boolean getTradableKeyWayMold() {
        return tradableKeyWayMold;
    }
}
