package com.Minor2CCh.retrial_key.fabric.client;

import com.Minor2CCh.retrial_key.config.ModConfigLoader;
import com.Minor2CCh.retrial_key.item.RetrialKeyItem;
import com.Minor2CCh.retrial_key.item.UnstableRetrialKeyItem;
import com.Minor2CCh.retrial_key.registry.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public final class Retrial_keyFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        ItemTooltipCallback.EVENT.register((ItemStack stack, Item.TooltipContext _, TooltipFlag _, java.util.List<Component> lines) -> {
            if (stack.getItem() instanceof UnstableRetrialKeyItem) {
                lines.add(Component.translatable(stack.getItem().getDescriptionId()+".desc", ModConfigLoader.getConfig().getUnstableEventProbably() * 100).withStyle(ChatFormatting.WHITE));
            } else if (stack.getItem() instanceof RetrialKeyItem) {
                lines.add(Component.translatable(stack.getItem().getDescriptionId()+".desc", ModItems.KEY_DURABILITY).withStyle(ChatFormatting.WHITE));
            }
        });
    }
}
