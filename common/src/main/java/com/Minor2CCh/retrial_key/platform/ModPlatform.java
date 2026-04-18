package com.Minor2CCh.retrial_key.platform;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public interface ModPlatform {
    ModPlatform INSTANCE = ((Supplier<ModPlatform>)(() -> {
        ServiceLoader<ModPlatform> loader = ServiceLoader.load(ModPlatform.class);
        Iterator<ModPlatform> iterator = loader.iterator();
        if (!iterator.hasNext()) {
            throw new RuntimeException("Platform instance not found!");
        }
        ModPlatform platform = iterator.next();
        if (iterator.hasNext()) {
            throw new RuntimeException("More than one platform instance was found!");
        }
        return platform;
    })).get();
    enum ModLoader{
        NEOFORGE,
        FABRIC
    }
    ModLoader getModLoader();
    Path getConfigPath();
    <T extends Item> Supplier<T> itemRegister(String id, Supplier<T> item);
    Supplier<CreativeModeTab> creativeModeTabRegister(String id, Supplier<ItemStack> stack, List<Supplier<Object>> stacks);
}
