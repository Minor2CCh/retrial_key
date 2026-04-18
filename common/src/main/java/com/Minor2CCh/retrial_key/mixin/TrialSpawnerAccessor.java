package com.Minor2CCh.retrial_key.mixin;

import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerStateData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TrialSpawnerStateData.class)
public interface TrialSpawnerAccessor {
    @Accessor("cooldownEndsAt")
    long cooldownEnd();
    @Accessor("cooldownEndsAt")
    void setCooldownEnd(long newCooldownEnd);
    @Accessor("totalMobsSpawned")
    void setTotalSpawnedMobs(int TotalSpawnedMobs);
}

