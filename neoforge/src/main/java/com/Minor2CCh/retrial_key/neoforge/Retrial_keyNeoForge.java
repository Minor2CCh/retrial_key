package com.Minor2CCh.retrial_key.neoforge;

import com.Minor2CCh.retrial_key.Retrial_key;
import com.Minor2CCh.retrial_key.neoforge.platform.NeoForgePlatform;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Retrial_key.MOD_ID)
public final class Retrial_keyNeoForge {
    public Retrial_keyNeoForge(IEventBus modEventBus) {
        // Run our common setup.
        //EventBusesHooks.getModEventBus(Retrial_key.MOD_ID);
        Retrial_key.init();
        NeoForgePlatform.registryInit(modEventBus);
    }
}
