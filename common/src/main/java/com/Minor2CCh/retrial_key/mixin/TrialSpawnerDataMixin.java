package com.Minor2CCh.retrial_key.mixin;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.spawner.MobSpawnerEntry;
import net.minecraft.block.spawner.TrialSpawnerData;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Uuids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TrialSpawnerData.class)
public class TrialSpawnerDataMixin {

    @Shadow
    @Mutable
    public static MapCodec<TrialSpawnerData> codec;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void replaceCodec(CallbackInfo ci) {
        codec = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Uuids.SET_CODEC.lenientOptionalFieldOf("registered_players", Sets.newHashSet()).forGetter(data -> ((TrialSpawnerAccessor)data).getPlayers()),
                        Uuids.SET_CODEC.lenientOptionalFieldOf("current_mobs", Sets.newHashSet()).forGetter(data -> ((TrialSpawnerAccessor)data).getSpawnedMobsAlive()),
                        Codec.LONG.lenientOptionalFieldOf("cooldown_ends_at", 0L).forGetter(data -> ((TrialSpawnerAccessor)data).cooldownEnd()),
                        Codec.LONG.lenientOptionalFieldOf("next_mob_spawns_at", 0L).forGetter(data -> ((TrialSpawnerAccessor)data).getNextMobSpawnsAt()),
                        Codec.INT
                                .lenientOptionalFieldOf("total_mobs_spawned", 0)
                                .forGetter(data -> ((TrialSpawnerAccessor)data).getTotalSpawnedMobs()),
                        MobSpawnerEntry.CODEC.lenientOptionalFieldOf("spawn_data").forGetter(data -> ((TrialSpawnerAccessor)data).getSpawnData()),
                        RegistryKey.createCodec(RegistryKeys.LOOT_TABLE).lenientOptionalFieldOf("ejecting_loot_table").forGetter(data -> ((TrialSpawnerAccessor)data).getRewardLootTable())
                ).apply(instance, TrialSpawnerData::new)
        );
    }
}