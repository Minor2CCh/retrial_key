package com.Minor2CCh.retrial_key.neoforge;

import com.Minor2CCh.retrial_key.Retrial_key;
import com.Minor2CCh.retrial_key.config.ModConfigLoader;
import com.Minor2CCh.retrial_key.item.RetrialKeyItem;
import com.Minor2CCh.retrial_key.item.UnstableRetrialKeyItem;
import com.Minor2CCh.retrial_key.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(modid = Retrial_key.MOD_ID, value = Dist.CLIENT)
public class Retrial_keyNeoForgeClient {
    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        if (event.getItemStack().getItem() instanceof UnstableRetrialKeyItem) {
            event.getToolTip().add(Component.translatable(event.getItemStack().getItem().getDescriptionId()+".desc", ModConfigLoader.getConfig().getUnstableEventProbably() * 100).withStyle(ChatFormatting.WHITE));
        } else if (event.getItemStack().getItem() instanceof RetrialKeyItem) {
            event.getToolTip().add(Component.translatable(event.getItemStack().getItem().getDescriptionId()+".desc", ModItems.KEY_DURABILITY).withStyle(ChatFormatting.WHITE));
        }
    }
}
