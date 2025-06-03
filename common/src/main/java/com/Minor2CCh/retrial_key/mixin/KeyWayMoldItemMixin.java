package com.Minor2CCh.retrial_key.mixin;

import com.Minor2CCh.retrial_key.item.KeyWayMoldItem;
import com.Minor2CCh.retrial_key.registry.ModItems;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(Item.class)
public class KeyWayMoldItemMixin {
    @ModifyReturnValue(method = "getRecipeRemainder()Lnet/minecraft/item/Item;", at = @At("RETURN"))
    public Item KeyWayMoldSave(Item original){
        Item modifyRemainderItem = (Item) (Object) this;
        if(modifyRemainderItem instanceof KeyWayMoldItem){
            return modifyRemainderItem;
        }
        return original;
    }
    @ModifyReturnValue(method = "Lnet/minecraft/item/Item;hasRecipeRemainder()Z", at = @At("RETURN"))
    public boolean disguiseHasRecipeRemainder(boolean original){

        Item modifyRemainderItem = (Item) (Object) this;
        if(modifyRemainderItem instanceof KeyWayMoldItem){
            return true;
        }
        return original;
    }
}
