package com.Minor2CCh.retrial_key.registry;

import com.Minor2CCh.retrial_key.Retrial_key;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;

public class ModItemGroup {
    public static final DeferredRegister<ItemGroup> TABS =
            DeferredRegister.create(Retrial_key.MOD_ID, RegistryKeys.ITEM_GROUP);

    public static final RegistrySupplier<ItemGroup> TAB_RETRIAL_KEY = TABS.register("retrial_key_group",
            () -> CreativeTabRegistry.create(
                    Text.translatable("itemGroup.retrial_key"),
                    () ->new ItemStack(ModItems.RETRIAL_KEY.get())
            )
    );
    public static void init(){
        TABS.register();
    }
}
