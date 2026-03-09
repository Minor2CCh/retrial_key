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
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TrialSpawnerData.Packed.class)
public class TrialSpawnerDataMixin {

    @Final
    @Shadow
    @Mutable
    public static MapCodec<TrialSpawnerData.Packed> CODEC;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void replaceCodec(CallbackInfo ci) {
        CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Uuids.SET_CODEC.lenientOptionalFieldOf("registered_players", Sets.newHashSet()).forGetter(TrialSpawnerData.Packed::detectedPlayers),
                        Uuids.SET_CODEC.lenientOptionalFieldOf("current_mobs", Sets.newHashSet()).forGetter(TrialSpawnerData.Packed::currentMobs),
                        Codec.LONG.lenientOptionalFieldOf("cooldown_ends_at", 0L).forGetter(TrialSpawnerData.Packed::cooldownEndsAt),
                        Codec.LONG.lenientOptionalFieldOf("next_mob_spawns_at", 0L).forGetter(TrialSpawnerData.Packed::nextMobSpawnsAt),
                        Codec.INT
                                .lenientOptionalFieldOf("total_mobs_spawned", 0)
                                .forGetter(TrialSpawnerData.Packed::totalMobsSpawned),
                        MobSpawnerEntry.CODEC.lenientOptionalFieldOf("spawn_data").forGetter(TrialSpawnerData.Packed::nextSpawnData),
                        RegistryKey.createCodec(RegistryKeys.LOOT_TABLE).lenientOptionalFieldOf("ejecting_loot_table").forGetter(TrialSpawnerData.Packed::ejectingLootTable)
                ).apply(instance, TrialSpawnerData.Packed::new)
        );
    }
}