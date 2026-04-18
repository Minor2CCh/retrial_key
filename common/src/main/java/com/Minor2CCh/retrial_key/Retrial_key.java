package com.Minor2CCh.retrial_key;

import com.Minor2CCh.retrial_key.config.ModConfigLoader;
import com.Minor2CCh.retrial_key.registry.ModCreativeModeTab;
import com.Minor2CCh.retrial_key.registry.ModItems;
import net.minecraft.resources.Identifier;

public final class Retrial_key {
    public static final String MOD_ID = "retrial_key";
    public static void init() {
        ModConfigLoader.load();

        ModItems.init();
        ModCreativeModeTab.init();
        // Write common init code here.
    }
    public static Identifier of(String id){
        return Identifier.fromNamespaceAndPath(MOD_ID, id);
    }
}
