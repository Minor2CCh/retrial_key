package com.Minor2CCh.retrial_key.mixin;

import net.minecraft.block.spawner.MobSpawnerEntry;
import net.minecraft.block.spawner.TrialSpawnerData;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Mixin(TrialSpawnerData.class)
public interface TrialSpawnerAccessor {
    @Accessor("cooldownEnd")
    long cooldownEnd();
    @Accessor("cooldownEnd")
    void setCooldownEnd(long newCooldownEnd);
    @Accessor("totalSpawnedMobs")
    void setTotalSpawnedMobs(int TotalSpawnedMobs);
    @Accessor("totalSpawnedMobs")
    int getTotalSpawnedMobs();
    @Accessor("players")
    Set<UUID> getPlayers();
    @Accessor("spawnedMobsAlive")
    Set<UUID> getSpawnedMobsAlive();
    @Accessor("nextMobSpawnsAt")
    long getNextMobSpawnsAt();
    @Accessor("spawnData")
    Optional<MobSpawnerEntry> getSpawnData();
    @Accessor("rewardLootTable")
    Optional<RegistryKey<LootTable>> getRewardLootTable();
}

