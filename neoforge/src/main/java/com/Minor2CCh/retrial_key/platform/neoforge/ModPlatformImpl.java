package com.Minor2CCh.retrial_key.platform.neoforge;

import com.Minor2CCh.retrial_key.Retrial_key;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
@SuppressWarnings("unused")
@EventBusSubscriber(modid = Retrial_key.MOD_ID)
public class ModPlatformImpl {
    public static void registerVillagerTrade(VillagerProfession profession, int lv, TradeOffers.Factory factory){
        TRADE_LISTENERS.add((event) -> {
            // 司書のみ対象
            if (event.getType() == profession) {

                // レベル2（Apprentice）
                List<TradeOffers.Factory> level2Trades = event.getTrades().get(lv);

                level2Trades.add(factory);
            }

        });
    }
    private static final List<Consumer<VillagerTradesEvent>> TRADE_LISTENERS = new ArrayList<>();
    @SubscribeEvent
    public static void registerVillagerTrades(VillagerTradesEvent event){
        for(Consumer<VillagerTradesEvent> consumer : TRADE_LISTENERS){
            consumer.accept(event);
        }
    }
}
