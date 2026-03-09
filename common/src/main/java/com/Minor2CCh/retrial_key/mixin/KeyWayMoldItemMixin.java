package com.Minor2CCh.retrial_key.mixin;

import com.Minor2CCh.retrial_key.registry.ModItems;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;


@Mixin(Item.class)
public class KeyWayMoldItemMixin {
    @ModifyReturnValue(method = "Lnet/minecraft/item/Item;getRecipeRemainder()Lnet/minecraft/item/ItemStack;", at = @At("RETURN"))
    public ItemStack KeyWayMoldSave(ItemStack original){
        Item modifyRemainderItem = (Item) (Object) this;
        if(Objects.equals(modifyRemainderItem, ModItems.KEY_WAY_MOLD.get())){
            return modifyRemainderItem.getDefaultStack();
        }
        return original;
    }
}
