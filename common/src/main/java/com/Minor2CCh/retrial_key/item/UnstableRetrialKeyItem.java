package com.Minor2CCh.retrial_key.item;

import com.Minor2CCh.retrial_key.config.ModConfigLoader;
import com.Minor2CCh.retrial_key.mixin.TrialSpawnerAccessor;
import com.Minor2CCh.retrial_key.registry.ModItems;
import com.google.common.base.Suppliers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.TrialSpawnerBlock;
import net.minecraft.block.entity.TrialSpawnerBlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.WindChargeEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.AdvancedExplosionBehavior;
import net.minecraft.world.explosion.ExplosionBehavior;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class UnstableRetrialKeyItem extends RetrialKeyItem{
    public UnstableRetrialKeyItem(Settings settings) {
        super(settings);
    }
    private static final double UNSTABLE_PROBABILITY = Math.min(1, Math.max(ModConfigLoader.getConfig().getUnstableEventProbably(), 0));
    private static final Supplier<ExplosionBehavior> EXPLOSION_BEHAVIOR = Suppliers.memoize(() -> new AdvancedExplosionBehavior(
            true, false, Optional.of(1.22F), Registries.BLOCK.getOptional(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity())));
    private static final int BOX_RADIUS = 5;
    @Override
    protected void modifyFromDispenser(BlockPointer pointer, boolean enforcementSkip){
        ServerWorld world = pointer.world();
        Direction dir = pointer.state().get(DispenserBlock.FACING);
        BlockPos pos = pointer.pos().offset(dir);
        Random random = new Random();
        TrialSpawnerBlockEntity trialSpawnerBlockEntity = (TrialSpawnerBlockEntity) world.getBlockEntity(pos);
        if(trialSpawnerBlockEntity == null){
            return;
        }
        if(random.nextFloat(1) < UNSTABLE_PROBABILITY){
            int forkID = random.nextInt(8);
            if(!world.isClient()){
                playersConsumer(pointer, (playerEntity) -> playerEntity.sendMessage(Text.translatable(ModItems.UNSTABLE_RETRIAL_KEY.get().getTranslationKey()+".fail."+forkID), true));
            }
            switch(forkID){
                case 0:
                    break;
                case 1:
                    super.modifyFromDispenser(pointer, true);
                    BlockState blockState1 = world.getBlockState(pos);
                    world.setBlockState(pos, blockState1.with(TrialSpawnerBlock.OMINOUS, Boolean.TRUE));
                    break;
                case 2:
                    super.modifyFromDispenser(pointer, false);
                    playersConsumer(pointer, (playerEntity) -> {
                        playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 200, 0, false, false, true), null);
                        playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 200, 0, false, false, true), null);
                    });
                    break;
                case 3:
                    super.modifyFromDispenser(pointer, false);
                    int dropPotato = random.nextInt(2, Items.POISONOUS_POTATO.getMaxCount());
                    for(int i=0;i < dropPotato;i++){
                        Block.dropStack(world, pos, Items.POISONOUS_POTATO.getDefaultStack());
                    }
                    break;
                case 4:
                    super.modifyFromDispenser(pointer, false);
                    playersConsumer(pointer, (playerEntity) -> {
                        playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 1, false, false, true), null);
                        playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 3, false, false, true), null);
                    });
                    break;
                case 5:
                    super.modifyFromDispenser(pointer, true);
                    ((TrialSpawnerAccessor)(trialSpawnerBlockEntity.getSpawner().getData())).setTotalSpawnedMobs(-6);
                    if(nearOminousPlayer(pointer)){
                        BlockState blockState5 = world.getBlockState(pos);
                        world.setBlockState(pos, blockState5.with(TrialSpawnerBlock.OMINOUS, Boolean.TRUE));
                    }
                    break;
                case 6:
                    super.modifyFromDispenser(pointer, false);
                    WindChargeEntity windChargeEntity = new WindChargeEntity(world, pos.getX()+0.5, pos.getY()+1, pos.getZ()+0.5, new Vec3d(0, -0.125, 0));
                    world.createExplosion(
                            windChargeEntity,
                            null,
                            EXPLOSION_BEHAVIOR.get(),
                            windChargeEntity.getX(),
                            windChargeEntity.getY(),
                            windChargeEntity.getZ(),
                            2.4F,
                            false,
                            World.ExplosionSourceType.TRIGGER,
                            ParticleTypes.GUST_EMITTER_SMALL,
                            ParticleTypes.GUST_EMITTER_LARGE,
                            SoundEvents.ENTITY_WIND_CHARGE_WIND_BURST
                    );
                    break;
                case 7:
                    super.modifyFromDispenser(pointer, false);
                    playersConsumer(pointer, (playerEntity) -> {
                        for (int i = 0; i < 16; i++) {
                            double d = playerEntity.getX() + (playerEntity.getRandom().nextDouble() - 0.5) * 16.0;
                            double e = MathHelper.clamp(
                                    playerEntity.getY() + (playerEntity.getRandom().nextInt(16) - 8), world.getBottomY(), (world.getBottomY() + world.getLogicalHeight() - 1)
                            );
                            double f = playerEntity.getZ() + (playerEntity.getRandom().nextDouble() - 0.5) * 16.0;
                            if (playerEntity.hasVehicle()) {
                                playerEntity.stopRiding();
                            }
                            Vec3d vec3d = playerEntity.getPos();
                            if (playerEntity.teleport(d, e, f, true)) {
                                world.emitGameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Emitter.of(playerEntity));
                                SoundCategory soundCategory;
                                SoundEvent soundEvent;
                                soundEvent = SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
                                soundCategory = SoundCategory.PLAYERS;
                                world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), soundEvent, soundCategory);
                                playerEntity.onLanding();
                                break;
                            }
                            playerEntity.clearCurrentExplosion();
                        }
                    });
                    break;
            }
        }else{
            super.modifyFromDispenser(pointer, false);
        }
    }
    protected boolean nearOminousPlayer(BlockPointer pointer){
        ServerWorld world = pointer.world();
        Direction dir = pointer.state().get(DispenserBlock.FACING);
        BlockPos pos = pointer.pos().offset(dir);
        final int radius = BOX_RADIUS;

        Box box = new Box(
                pos.getX() - radius,
                pos.getY() - radius,
                pos.getZ() - radius,
                pos.getX() + radius + 1,
                pos.getY() + radius + 1,
                pos.getZ() + radius + 1
        );
        List<PlayerEntity> players = world.getNonSpectatingEntities(PlayerEntity.class, box);
        for(PlayerEntity player : players){
            if(player.hasStatusEffect(StatusEffects.TRIAL_OMEN) || player.hasStatusEffect(StatusEffects.BAD_OMEN)){
                return true;
            }
        }
        return false;
    }
    protected void playersConsumer(BlockPointer pointer, Consumer<PlayerEntity> consumer){
        ServerWorld world = pointer.world();
        Direction dir = pointer.state().get(DispenserBlock.FACING);
        BlockPos pos = pointer.pos().offset(dir);
        final int radius = BOX_RADIUS;

        Box box = new Box(
                pos.getX() - radius,
                pos.getY() - radius,
                pos.getZ() - radius,
                pos.getX() + radius + 1,
                pos.getY() + radius + 1,
                pos.getZ() + radius + 1
        );
        List<PlayerEntity> players = world.getNonSpectatingEntities(PlayerEntity.class, box);
        for(PlayerEntity player : players){
            consumer.accept(player);
        }
    }

    @Override
    protected void modifyTrialSpawner(ItemUsageContext context, World world, BlockPos blockPos, PlayerEntity playerEntity, boolean enforcementSkip){
        Random random = new Random();
        TrialSpawnerBlockEntity trialSpawnerBlockEntity = (TrialSpawnerBlockEntity) world.getBlockEntity(blockPos);
        if(trialSpawnerBlockEntity == null){
            return;
        }
        if(random.nextFloat(1) < UNSTABLE_PROBABILITY){
            int forkID;
                switch(forkID = random.nextInt(8)){
                    case 0:
                        break;
                    case 1:
                        super.modifyTrialSpawner(context, world, blockPos, playerEntity, true);
                        BlockState blockState1 = world.getBlockState(blockPos);
                        world.setBlockState(blockPos, blockState1.with(TrialSpawnerBlock.OMINOUS, Boolean.TRUE));
                        break;
                    case 2:
                        super.modifyTrialSpawner(context, world, blockPos, playerEntity, false);
                        playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 200, 0, false, false, true), null);
                        playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 200, 0, false, false, true), null);
                        break;
                    case 3:
                        super.modifyTrialSpawner(context, world, blockPos, playerEntity, false);
                        int dropPotato = random.nextInt(2, Items.POISONOUS_POTATO.getMaxCount());
                        for(int i=0;i < dropPotato;i++){
                            Block.dropStack(world, blockPos, Items.POISONOUS_POTATO.getDefaultStack());
                        }
                        break;
                    case 4:
                        super.modifyTrialSpawner(context, world, blockPos, playerEntity, false);
                        playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 1, false, false, true), null);
                        playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 3, false, false, true), null);
                        break;
                    case 5:
                        super.modifyTrialSpawner(context, world, blockPos, playerEntity, true);
                        ((TrialSpawnerAccessor)(trialSpawnerBlockEntity.getSpawner().getData())).setTotalSpawnedMobs(-6);
                        if(playerEntity.hasStatusEffect(StatusEffects.TRIAL_OMEN) || playerEntity.hasStatusEffect(StatusEffects.BAD_OMEN)){
                            BlockState blockState5 = world.getBlockState(blockPos);
                            world.setBlockState(blockPos, blockState5.with(TrialSpawnerBlock.OMINOUS, Boolean.TRUE));
                        }
                        break;
                    case 6:
                        super.modifyTrialSpawner(context, world, blockPos, playerEntity, false);
                        WindChargeEntity windChargeEntity = new WindChargeEntity(world, context.getHitPos().getX(), context.getHitPos().getY(), context.getHitPos().getZ(), new Vec3d(0, -0.125, 0));
                            world.createExplosion(
                                    windChargeEntity,
                                    null,
                                    EXPLOSION_BEHAVIOR.get(),
                                    windChargeEntity.getX(),
                                    windChargeEntity.getY(),
                                    windChargeEntity.getZ(),
                                    2.4F,
                                    false,
                                    World.ExplosionSourceType.TRIGGER,
                                    ParticleTypes.GUST_EMITTER_SMALL,
                                    ParticleTypes.GUST_EMITTER_LARGE,
                                    SoundEvents.ENTITY_WIND_CHARGE_WIND_BURST
                            );
                        break;
                    case 7:
                        super.modifyTrialSpawner(context, world, blockPos, playerEntity, false);
                        for (int i = 0; i < 16; i++) {
                            double d = playerEntity.getX() + (playerEntity.getRandom().nextDouble() - 0.5) * 16.0;
                            double e = MathHelper.clamp(
                                    playerEntity.getY() + (playerEntity.getRandom().nextInt(16) - 8), world.getBottomY(), (world.getBottomY() + ((ServerWorld)world).getLogicalHeight() - 1)
                            );
                            double f = playerEntity.getZ() + (playerEntity.getRandom().nextDouble() - 0.5) * 16.0;
                            if (playerEntity.hasVehicle()) {
                                playerEntity.stopRiding();
                            }
                            Vec3d vec3d = playerEntity.getPos();
                            if (playerEntity.teleport(d, e, f, true)) {
                                world.emitGameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Emitter.of(playerEntity));
                                SoundCategory soundCategory;
                                SoundEvent soundEvent;
                                soundEvent = SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
                                soundCategory = SoundCategory.PLAYERS;
                                world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), soundEvent, soundCategory);
                                playerEntity.onLanding();
                                break;
                            }
                        }
                        playerEntity.clearCurrentExplosion();
                        playerEntity.getItemCooldownManager().set(context.getStack(), 20);
                        break;
                }
            if(!world.isClient()){
                playerEntity.sendMessage(Text.translatable(ModItems.UNSTABLE_RETRIAL_KEY.get().getTranslationKey()+".fail."+forkID), true);
            }
        }else{
            super.modifyTrialSpawner(context, world, blockPos, playerEntity, false);
        }
    }
}
