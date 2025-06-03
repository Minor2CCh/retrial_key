package com.Minor2CCh.retrial_key.fabric;

import com.Minor2CCh.retrial_key.Retrial_key;
import net.fabricmc.api.ModInitializer;

public final class Retrial_keyFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.

        Retrial_key.init();
    }
}
