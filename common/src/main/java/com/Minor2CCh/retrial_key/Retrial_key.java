package com.Minor2CCh.retrial_key;

import com.Minor2CCh.retrial_key.registry.ModItemGroup;
import com.Minor2CCh.retrial_key.registry.ModItems;
import com.google.common.base.Suppliers;
import dev.architectury.registry.registries.RegistrarManager;

import java.util.function.Supplier;

public final class Retrial_key {
    public static final String MOD_ID = "retrial_key";

    public static final Supplier<RegistrarManager> REGISTRIES = Suppliers.memoize(() -> RegistrarManager.get(Retrial_key.MOD_ID));
    public static void init() {
        //ModConfigLoader.initialize();


        ModItems.init();
        ModItemGroup.init();
        // Write common init code here.
    }
}
