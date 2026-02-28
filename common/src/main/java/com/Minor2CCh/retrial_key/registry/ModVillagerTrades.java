package com.Minor2CCh.retrial_key.registry;

import com.Minor2CCh.retrial_key.platform.ModPlatform;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import net.minecraft.village.VillagerProfession;

import java.util.Optional;

public final class ModVillagerTrades {
    private ModVillagerTrades() {}
    public static void init(){
        ModPlatform.registerVillagerTrade(VillagerProfession.TOOLSMITH, 2, (entity, random) ->
                getKeyWayMoldTrade());
        ModPlatform.registerVillagerTrade(VillagerProfession.ARMORER, 3, (entity, random) ->
                getKeyWayMoldTrade());
        ModPlatform.registerVillagerTrade(VillagerProfession.WEAPONSMITH, 3, (entity, random) ->
                getKeyWayMoldTrade());

    }
    private static TradeOffer getKeyWayMoldTrade(){
        return new TradeOffer(
                new TradedItem(Items.EMERALD, 19),
                Optional.of(new TradedItem(Items.IRON_BLOCK, 5)),
                new ItemStack(ModItems.KEY_WAY_MOLD.get()),
                4,
                60,
                0.2f
        );
    }
}
