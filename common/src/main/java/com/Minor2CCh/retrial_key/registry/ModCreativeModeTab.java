package com.Minor2CCh.retrial_key.registry;

import com.Minor2CCh.retrial_key.platform.ModPlatform;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Supplier;

public final class ModCreativeModeTab {
    private ModCreativeModeTab() {}
    @SuppressWarnings("unused")
    public static final Supplier<CreativeModeTab> TAB_RETRIAL_KEY = ModPlatform.INSTANCE.creativeModeTabRegister("retrial_key_group",
            () -> new ItemStack(ModItems.RETRIAL_KEY.get()), List.of(
                    (ModItems.RETRIAL_KEY::get),
                    (ModItems.KEY_WAY::get),
                    (ModItems.KEY_WAY_MOLD::get),
                    (ModItems.HEAVY_RETRIAL_KEY::get),
                    (ModItems.INFINITE_RETRIAL_KEY::get),
                    (ModItems.TRIAL_CORE::get),
                    (ModItems.UNSTABLE_RETRIAL_KEY::get)
            ));
    public static void init(){
    }
}
