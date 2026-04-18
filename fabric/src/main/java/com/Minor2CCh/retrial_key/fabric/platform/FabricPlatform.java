package com.Minor2CCh.retrial_key.fabric.platform;

import com.Minor2CCh.retrial_key.Retrial_key;
import com.Minor2CCh.retrial_key.platform.ModPlatform;
import com.google.auto.service.AutoService;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;

@AutoService(ModPlatform.class)
public class FabricPlatform implements ModPlatform {
    @Override
    public ModLoader getModLoader() {
        return ModLoader.FABRIC;
    }
    @Override
    public Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir();
    }
    @Override
    public <T extends Item> Supplier<T> itemRegister(String id, Supplier<T> item) {
        T itemInfo = item.get();
        Identifier blockID = Retrial_key.of(id);
        Registry.register(BuiltInRegistries.ITEM, blockID, itemInfo);
        return () -> itemInfo;
    }

    @Override
    public Supplier<CreativeModeTab> creativeModeTabRegister(String id, Supplier<ItemStack> stack, List<Supplier<Object>> items) {
        CreativeModeTab tab = FabricCreativeModeTab.builder()
                .icon(stack)
                .title(Component.translatable(String.format("itemGroup.%s.%s", Retrial_key.MOD_ID, id)))
                .displayItems((_, output) -> {
                    for(Supplier<Object> item : items){
                        if(item.get() instanceof ItemLike itemLike){
                            output.accept(itemLike);
                        }else if(item.get() instanceof ItemStack itemStack){
                            output.accept(itemStack);
                        }
                    }
                })
                .build();
        ResourceKey<CreativeModeTab> key = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), Retrial_key.of(id));
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, tab);
        return () -> tab;
    }
}
