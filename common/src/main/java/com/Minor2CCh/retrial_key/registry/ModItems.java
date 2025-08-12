package com.Minor2CCh.retrial_key.registry;


import com.Minor2CCh.retrial_key.Retrial_key;
import com.Minor2CCh.retrial_key.config.ModConfigLoader;
import com.Minor2CCh.retrial_key.item.*;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Retrial_key.MOD_ID, RegistryKeys.ITEM);
    public static final int KEY_DURABILITY = ModConfigLoader.getConfig().heavyRetrialKeyDurability;
    public static final RegistrySupplier<Item> RETRIAL_KEY = registerItem("retrial_key", () -> new RetrialKeyItem(new Item.Settings().arch$tab(ModItemGroup.TAB_RETRIAL_KEY).registryKey(keyOfItem("retrial_key"))));
    public static final RegistrySupplier<Item> KEY_WAY = registerItem("key_way", () -> new Item(new Item.Settings().arch$tab(ModItemGroup.TAB_RETRIAL_KEY).registryKey(keyOfItem("key_way"))));
    public static final RegistrySupplier<Item> KEY_WAY_MOLD = registerItem("key_way_mold", () -> new KeyWayMoldItem(new Item.Settings().arch$tab(ModItemGroup.TAB_RETRIAL_KEY).maxCount(1).registryKey(keyOfItem("key_way_mold"))));
    public static final RegistrySupplier<Item> HEAVY_RETRIAL_KEY = registerItem("heavy_retrial_key", () -> new RetrialKeyItem(new Item.Settings().arch$tab(ModItemGroup.TAB_RETRIAL_KEY).maxDamage(getKeyDurability()).rarity(Rarity.EPIC).registryKey(keyOfItem("heavy_retrial_key"))));
    public static final RegistrySupplier<Item> INFINITE_RETRIAL_KEY = registerItem("infinite_retrial_key", () -> new RetrialKeyItem(new Item.Settings().arch$tab(ModItemGroup.TAB_RETRIAL_KEY).maxCount(1).rarity(Rarity.EPIC).registryKey(keyOfItem("infinite_retrial_key"))));
    public static final RegistrySupplier<Item> TRIAL_CORE = registerItem("trial_core", () -> new Item(new Item.Settings().arch$tab(ModItemGroup.TAB_RETRIAL_KEY).registryKey(keyOfItem("trial_core"))));
    public static final RegistrySupplier<Item> UNSTABLE_RETRIAL_KEY = registerItem("unstable_retrial_key", () -> new UnstableRetrialKeyItem(new Item.Settings().arch$tab(ModItemGroup.TAB_RETRIAL_KEY).registryKey(keyOfItem("unstable_retrial_key"))));

    private static <T extends Item> RegistrySupplier<T> registerItem(String idPath, Supplier<T> item) {
        return ITEMS.register(idPath, item);

    }

    public static void init(){
        ITEMS.register();
    }
    public static int getKeyDurability(){
        return Math.max(KEY_DURABILITY, 1);
    }
    private static RegistryKey<Item> keyOfItem(String name) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Retrial_key.MOD_ID, name));
    }


}
