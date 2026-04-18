package com.Minor2CCh.retrial_key.neoforge.platform;

import com.Minor2CCh.retrial_key.Retrial_key;
import com.Minor2CCh.retrial_key.platform.ModPlatform;
import com.google.auto.service.AutoService;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;

@AutoService(ModPlatform.class)
@EventBusSubscriber(modid = Retrial_key.MOD_ID)
public class NeoForgePlatform implements ModPlatform {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Retrial_key.MOD_ID);
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Retrial_key.MOD_ID);
    @Override
    public ModLoader getModLoader() {
        return ModLoader.NEOFORGE;
    }
    @Override
    public Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get();
    }
    @Override
    public <T extends Item> Supplier<T> itemRegister(String id, Supplier<T> item) {
        return ITEMS.register(id, item);
    }

    @Override
    public Supplier<CreativeModeTab> creativeModeTabRegister(String id, Supplier<ItemStack> stack,  List<Supplier<Object>> items) {
        return CREATIVE_MODE_TABS.register(id, () -> CreativeModeTab.builder().icon(stack)
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
                .build());
    }
    public static void registryInit(IEventBus modEventBus){
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
