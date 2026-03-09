package com.Minor2CCh.retrial_key.platform.fabric;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.registry.RegistryKey;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

@SuppressWarnings("unused")
public class ModPlatformImpl {
    public static void registerVillagerTrade(RegistryKey<VillagerProfession> profession, int lv, TradeOffers.Factory factory){
        TradeOfferHelper.registerVillagerOffers(
                profession,
                lv,
                factories -> factories.add(factory)
        );
    }
}
