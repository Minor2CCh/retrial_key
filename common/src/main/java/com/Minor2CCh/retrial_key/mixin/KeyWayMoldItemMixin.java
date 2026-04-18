package com.Minor2CCh.retrial_key.mixin;

import com.Minor2CCh.retrial_key.registry.ModItems;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;


@Mixin(Item.class)
public class KeyWayMoldItemMixin {
    @ModifyReturnValue(method = "getCraftingRemainder()Lnet/minecraft/world/item/ItemStackTemplate;", at = @At("RETURN"))
    public ItemStackTemplate KeyWayMoldSave(ItemStackTemplate original){
        Item modifyRemainderItem = (Item) (Object) this;
        if(Objects.equals(modifyRemainderItem, ModItems.KEY_WAY_MOLD.get())){
            return new ItemStackTemplate(modifyRemainderItem);
        }
        return original;
    }
}
