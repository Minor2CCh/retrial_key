package com.Minor2CCh.retrial_key.fabric.client;

import com.Minor2CCh.retrial_key.item.RetrialKeyItem;
import com.Minor2CCh.retrial_key.item.UnstableRetrialKeyItem;
import com.Minor2CCh.retrial_key.registry.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public final class Retrial_keyFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        ItemTooltipCallback.EVENT.register((ItemStack stack, Item.TooltipContext context, TooltipType tooltipType, java.util.List<Text> lines) -> {
            if (stack.getItem() instanceof UnstableRetrialKeyItem) {
                lines.add(Text.translatable(stack.getItem().getTranslationKey()+".desc", UnstableRetrialKeyItem.UNSTABLE_PROBABILITY * 100).formatted(Formatting.WHITE));
            } else if (stack.getItem() instanceof RetrialKeyItem) {
                lines.add(Text.translatable(stack.getItem().getTranslationKey()+".desc", ModItems.KEY_DURABILITY).formatted(Formatting.WHITE));
            }
        });
    }
}
