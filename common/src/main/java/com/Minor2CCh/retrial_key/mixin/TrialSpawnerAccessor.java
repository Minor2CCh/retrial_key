package com.Minor2CCh.retrial_key.mixin;

import net.minecraft.block.spawner.TrialSpawnerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TrialSpawnerData.class)
public interface TrialSpawnerAccessor {
    @Accessor("cooldownEnd")
    long cooldownEnd();
    @Accessor("cooldownEnd")
    void setCooldownEnd(long newCooldownEnd);
    @Accessor("totalSpawnedMobs")
    void setTotalSpawnedMobs(int TotalSpawnedMobs);
}

