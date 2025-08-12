package com.Minor2CCh.retrial_key.neoforge;

import com.Minor2CCh.retrial_key.Retrial_key;
import com.Minor2CCh.retrial_key.item.RetrialKeyItem;
import com.Minor2CCh.retrial_key.item.UnstableRetrialKeyItem;
import com.Minor2CCh.retrial_key.registry.ModItems;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(modid = Retrial_key.MOD_ID, value = Dist.CLIENT)
public class Retrial_keyNeoForgeClient {
    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        if (event.getItemStack().getItem() instanceof UnstableRetrialKeyItem) {
            event.getToolTip().add(Text.translatable(event.getItemStack().getItem().getTranslationKey()+".desc", UnstableRetrialKeyItem.UNSTABLE_PROBABILITY * 100).formatted(Formatting.WHITE));
        } else if (event.getItemStack().getItem() instanceof RetrialKeyItem) {
            event.getToolTip().add(Text.translatable(event.getItemStack().getItem().getTranslationKey()+".desc", ModItems.KEY_DURABILITY).formatted(Formatting.WHITE));
        }
    }
}
