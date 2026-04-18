package com.Minor2CCh.retrial_key.registry;


import com.Minor2CCh.retrial_key.Retrial_key;
import com.Minor2CCh.retrial_key.config.ModConfigLoader;
import com.Minor2CCh.retrial_key.item.*;
import com.Minor2CCh.retrial_key.platform.ModPlatform;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class ModItems {
    private ModItems() {}
    public static final int KEY_DURABILITY = ModConfigLoader.getConfig().getHeavyRetrialKeyDurability();
    public static final Supplier<Item> RETRIAL_KEY = registerItem("retrial_key", (id) -> () -> new RetrialKeyItem(new Item.Properties().setId(keyOfItem(id))));
    public static final Supplier<Item> KEY_WAY = registerItem("key_way", (id) -> () -> new Item(new Item.Properties().setId(keyOfItem(id))));
    public static final Supplier<Item> KEY_WAY_MOLD = registerItem("key_way_mold", (id) -> () -> new Item(new Item.Properties().stacksTo(1).setId(keyOfItem(id))));
    public static final Supplier<Item> HEAVY_RETRIAL_KEY = registerItem("heavy_retrial_key", (id) -> () -> new RetrialKeyItem(new Item.Properties().durability(getKeyDurability()).rarity(Rarity.EPIC).setId(keyOfItem(id))));
    public static final Supplier<Item> INFINITE_RETRIAL_KEY = registerItem("infinite_retrial_key", (id) -> () -> new RetrialKeyItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).setId(keyOfItem(id))));
    public static final Supplier<Item> TRIAL_CORE = registerItem("trial_core", (id) -> () -> new Item(new Item.Properties().setId(keyOfItem(id))));
    public static final Supplier<Item> UNSTABLE_RETRIAL_KEY = registerItem("unstable_retrial_key", (id) -> () -> new UnstableRetrialKeyItem(new Item.Properties().setId(keyOfItem(id))));

    private static <T extends Item> Supplier<T> registerItem(String id, Function<String, Supplier<T>> item) {
        Supplier<T> supplierItem = item.apply(id);
        return ModPlatform.INSTANCE.itemRegister(id, supplierItem);

    }

    public static void init(){
    }
    public static int getKeyDurability(){
        return Math.max(KEY_DURABILITY, 1);
    }
    private static ResourceKey<Item> keyOfItem(String name) {
        return ResourceKey.create(Registries.ITEM, Retrial_key.of(name));
    }


}
