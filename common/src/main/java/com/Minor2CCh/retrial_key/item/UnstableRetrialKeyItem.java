package com.Minor2CCh.retrial_key.item;

import com.Minor2CCh.retrial_key.config.ModConfigLoader;
import com.Minor2CCh.retrial_key.mixin.TrialSpawnerAccessor;
import com.Minor2CCh.retrial_key.registry.ModItems;
import com.google.common.base.Suppliers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.TrialSpawnerBlock;
import net.minecraft.world.level.block.entity.TrialSpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class UnstableRetrialKeyItem extends RetrialKeyItem{
    public UnstableRetrialKeyItem(Item.Properties settings) {
        super(settings);
    }
    private static final double UNSTABLE_PROBABILITY = Math.clamp(ModConfigLoader.getConfig().getUnstableEventProbably(), 0, 1);
    private static final Supplier<ExplosionDamageCalculator> EXPLOSION_BEHAVIOR = Suppliers.memoize(() -> new SimpleExplosionDamageCalculator(
            true, false, Optional.of(1.22F), BuiltInRegistries.BLOCK.get(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity())));
    private static final int BOX_RADIUS = 5;
    @Override
    protected void modifyFromDispenser(BlockSource source, boolean enforcementSkip){
        ServerLevel level = source.level();
        Direction dir = source.state().getValue(DispenserBlock.FACING);
        BlockPos pos = source.pos().relative(dir);
        Random random = new Random();
        TrialSpawnerBlockEntity trialSpawnerBlockEntity = (TrialSpawnerBlockEntity) level.getBlockEntity(pos);
        if(trialSpawnerBlockEntity == null){
            return;
        }
        if(random.nextFloat(1) < UNSTABLE_PROBABILITY){
            int forkID = random.nextInt(8);
            if(!level.isClientSide()){
                playersConsumer(source, (player) -> player.sendOverlayMessage(Component.translatable(ModItems.UNSTABLE_RETRIAL_KEY.get().getDescriptionId() + ".fail." + forkID)));
            }
            switch(forkID){
                case 0:
                    break;
                case 1:
                    super.modifyFromDispenser(source, true);
                    BlockState blockState1 = level.getBlockState(pos);
                    level.setBlockAndUpdate(pos, blockState1.setValue(TrialSpawnerBlock.OMINOUS, Boolean.TRUE));
                    break;
                case 2:
                    super.modifyFromDispenser(source, false);
                    playersConsumer(source, (player) -> {
                        player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 200, 0, false, false, true), null);
                        player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200, 0, false, false, true), null);
                    });
                    break;
                case 3:
                    super.modifyFromDispenser(source, false);
                    int dropPotato = random.nextInt(2, Items.POISONOUS_POTATO.getDefaultMaxStackSize());
                    for(int i=0;i < dropPotato;i++){
                        Block.popResource(level, pos, Items.POISONOUS_POTATO.getDefaultInstance());
                    }
                    break;
                case 4:
                    super.modifyFromDispenser(source, false);
                    playersConsumer(source, (player) -> {
                        player.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 200, 1, false, false, true), null);
                        player.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 200, 3, false, false, true), null);
                    });
                    break;
                case 5:
                    super.modifyFromDispenser(source, true);
                    ((TrialSpawnerAccessor)(trialSpawnerBlockEntity.getTrialSpawner().getStateData())).setTotalSpawnedMobs(-6);
                    if(nearOminousPlayer(source)){
                        BlockState blockState5 = level.getBlockState(pos);
                        level.setBlockAndUpdate(pos, blockState5.setValue(TrialSpawnerBlock.OMINOUS, Boolean.TRUE));
                    }
                    break;
                case 6:
                    super.modifyFromDispenser(source, false);
                    WindCharge windChargeEntity = new WindCharge(level, pos.getX()+0.5, pos.getY()+1, pos.getZ()+0.5, new Vec3(0, -0.125, 0));
                    level.explode(
                            windChargeEntity,
                            null,
                            EXPLOSION_BEHAVIOR.get(),
                            windChargeEntity.getX(),
                            windChargeEntity.getY(),
                            windChargeEntity.getZ(),
                            2.4F,
                            false,
                            Level.ExplosionInteraction.TRIGGER,
                            ParticleTypes.GUST_EMITTER_SMALL,
                            ParticleTypes.GUST_EMITTER_LARGE,
                            WeightedList.of(),
                            SoundEvents.WIND_CHARGE_BURST
                    );
                    break;
                case 7:
                    super.modifyFromDispenser(source, false);
                    playersConsumer(source, (player) -> {
                        for (int i = 0; i < 16; i++) {
                            double d = player.getX() + (player.getRandom().nextDouble() - 0.5) * 16.0;
                            double e = Math.clamp(
                                    player.getY() + (player.getRandom().nextInt(16) - 8), level.getMinY(), (level.getMinY() + level.getLogicalHeight() - 1)
                            );
                            double f = player.getZ() + (player.getRandom().nextDouble() - 0.5) * 16.0;
                            if (player.isVehicle()) {
                                player.stopRiding();
                            }
                            Vec3 vec3d = player.position();
                            if (player.randomTeleport(d, e, f, true)) {
                                level.gameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Context.of(player));
                                SoundSource soundSource;
                                SoundEvent soundEvent;
                                soundEvent = SoundEvents.CHORUS_FRUIT_TELEPORT;
                                soundSource = SoundSource.PLAYERS;
                                level.playSound(player, player.getX(), player.getY(), player.getZ(), soundEvent, soundSource);
                                player.resetFallDistance();
                                player.resetCurrentImpulseContext();
                                break;
                            }
                        }
                    });
                    break;
            }
        }else{
            super.modifyFromDispenser(source, false);
        }
    }
    protected boolean nearOminousPlayer(BlockSource source){
        ServerLevel level = source.level();
        Direction dir = source.state().getValue(DispenserBlock.FACING);
        BlockPos pos = source.pos().relative(dir);
        final int radius = BOX_RADIUS;

        AABB box = new AABB(
                pos.getX() - radius,
                pos.getY() - radius,
                pos.getZ() - radius,
                pos.getX() + radius + 1,
                pos.getY() + radius + 1,
                pos.getZ() + radius + 1
        );
        List<Player> players = level.getEntitiesOfClass(Player.class, box);
        for(Player player : players){
            if(player.hasEffect(MobEffects.TRIAL_OMEN) || player.hasEffect(MobEffects.BAD_OMEN)){
                return true;
            }
        }
        return false;
    }
    protected void playersConsumer(BlockSource source, Consumer<Player> consumer){
        ServerLevel level = source.level();
        Direction dir = source.state().getValue(DispenserBlock.FACING);
        BlockPos pos = source.pos().relative(dir);
        final int radius = BOX_RADIUS;

        AABB box = new AABB(
                pos.getX() - radius,
                pos.getY() - radius,
                pos.getZ() - radius,
                pos.getX() + radius + 1,
                pos.getY() + radius + 1,
                pos.getZ() + radius + 1
        );
        List<Player> players = level.getEntitiesOfClass(Player.class, box);
        for(Player player : players){
            consumer.accept(player);
        }
    }

    @Override
    protected void modifyTrialSpawner(UseOnContext context, Level level, BlockPos blockPos, Player player, boolean enforcementSkip){
        Random random = new Random();
        TrialSpawnerBlockEntity trialSpawnerBlockEntity = (TrialSpawnerBlockEntity) level.getBlockEntity(blockPos);
        if(trialSpawnerBlockEntity == null){
            return;
        }
        if(random.nextFloat(1) < UNSTABLE_PROBABILITY){
            int forkID;
                switch(forkID = random.nextInt(8)){
                    case 0:
                        break;
                    case 1:
                        super.modifyTrialSpawner(context, level, blockPos, player, true);
                        BlockState blockState1 = level.getBlockState(blockPos);
                        level.setBlockAndUpdate(blockPos, blockState1.setValue(TrialSpawnerBlock.OMINOUS, Boolean.TRUE));
                        break;
                    case 2:
                        super.modifyTrialSpawner(context, level, blockPos, player, false);
                        player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 200, 0, false, false, true), null);
                        player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200, 0, false, false, true), null);
                        break;
                    case 3:
                        super.modifyTrialSpawner(context, level, blockPos, player, false);
                        int dropPotato = random.nextInt(2, Items.POISONOUS_POTATO.getDefaultMaxStackSize());
                        for(int i=0;i < dropPotato;i++){
                            Block.popResource(level, blockPos, Items.POISONOUS_POTATO.getDefaultInstance());
                        }
                        break;
                    case 4:
                        super.modifyTrialSpawner(context, level, blockPos, player, false);
                        player.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 200, 1, false, false, true), null);
                        player.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 200, 3, false, false, true), null);
                        break;
                    case 5:
                        super.modifyTrialSpawner(context, level, blockPos, player, true);
                        ((TrialSpawnerAccessor)(trialSpawnerBlockEntity.getTrialSpawner().getStateData())).setTotalSpawnedMobs(-6);
                        if(player.hasEffect(MobEffects.TRIAL_OMEN) || player.hasEffect(MobEffects.BAD_OMEN)){
                            BlockState blockState5 = level.getBlockState(blockPos);
                            level.setBlockAndUpdate(blockPos, blockState5.setValue(TrialSpawnerBlock.OMINOUS, Boolean.TRUE));
                        }
                        break;
                    case 6:
                        super.modifyTrialSpawner(context, level, blockPos, player, false);
                        WindCharge windChargeEntity = new WindCharge(level, context.getClickLocation().x(), context.getClickLocation().y(), context.getClickLocation().z(), new Vec3(0, -0.125, 0));
                            level.explode(
                                    windChargeEntity,
                                    null,
                                    EXPLOSION_BEHAVIOR.get(),
                                    windChargeEntity.getX(),
                                    windChargeEntity.getY(),
                                    windChargeEntity.getZ(),
                                    2.4F,
                                    false,
                                    Level.ExplosionInteraction.TRIGGER,
                                    ParticleTypes.GUST_EMITTER_SMALL,
                                    ParticleTypes.GUST_EMITTER_LARGE,
                                    WeightedList.of(),
                                    SoundEvents.WIND_CHARGE_BURST
                            );
                        break;
                    case 7:
                        super.modifyTrialSpawner(context, level, blockPos, player, false);
                        for (int i = 0; i < 16; i++) {
                            double d = player.getX() + (player.getRandom().nextDouble() - 0.5) * 16.0;
                            double e = Math.clamp(
                                    player.getY() + (player.getRandom().nextInt(16) - 8), level.getMinY(), (level.getMinY() + ((ServerLevel)level).getLogicalHeight() - 1)
                            );
                            double f = player.getZ() + (player.getRandom().nextDouble() - 0.5) * 16.0;
                            if (player.isVehicle()) {
                                player.stopRiding();
                            }
                            Vec3 vec3d = player.position();
                            if (player.randomTeleport(d, e, f, true)) {
                                level.gameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Context.of(player));
                                SoundSource soundCategory;
                                SoundEvent soundEvent;
                                soundEvent = SoundEvents.CHORUS_FRUIT_TELEPORT;
                                soundCategory = SoundSource.PLAYERS;
                                level.playSound(null, player.getX(), player.getY(), player.getZ(), soundEvent, soundCategory);
                                player.resetFallDistance();
                                player.resetCurrentImpulseContext();
                                break;
                            }
                        }
                        player.getCooldowns().addCooldown(context.getItemInHand(), 20);
                        break;
                }
            if(!level.isClientSide()){
                player.sendOverlayMessage(Component.translatable(ModItems.UNSTABLE_RETRIAL_KEY.get().getDescriptionId()+".fail."+forkID));
            }
        }else{
            super.modifyTrialSpawner(context, level, blockPos, player, false);
        }
    }
}
