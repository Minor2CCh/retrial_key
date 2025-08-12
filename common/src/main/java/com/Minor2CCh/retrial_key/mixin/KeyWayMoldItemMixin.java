package com.Minor2CCh.retrial_key.mixin;

import com.Minor2CCh.retrial_key.item.KeyWayMoldItem;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(Item.class)
public class KeyWayMoldItemMixin {
    @ModifyReturnValue(method = "Lnet/minecraft/item/Item;getRecipeRemainder()Lnet/minecraft/item/ItemStack;", at = @At("RETURN"))
    public ItemStack KeyWayMoldSave(ItemStack original){
        Item modifyRemainderItem = (Item) (Object) this;
        if(modifyRemainderItem instanceof KeyWayMoldItem){
            return modifyRemainderItem.getDefaultStack();
        }
        return original;
    }
    /*
    @ModifyReturnValue(method = "Lnet/minecraft/item/Item;hasRecipeRemainder()Z", at = @At("RETURN"))
    public boolean disguiseHasRecipeRemainder(boolean original){

        Item modifyRemainderItem = (Item) (Object) this;
        if(modifyRemainderItem instanceof KeyWayMoldItem){
            return true;
        }
        return original;
    }*/
}
