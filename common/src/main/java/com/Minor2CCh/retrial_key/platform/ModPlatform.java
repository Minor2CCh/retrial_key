package com.Minor2CCh.retrial_key.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.registry.RegistryKey;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

public class ModPlatform {
    @ExpectPlatform
    public static void registerVillagerTrade(RegistryKey<VillagerProfession> profession, int lv, TradeOffers.Factory factory){
        throw new AssertionError();
    }
}
