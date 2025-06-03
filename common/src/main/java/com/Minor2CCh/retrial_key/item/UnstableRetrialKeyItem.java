package com.Minor2CCh.retrial_key.item;

import com.Minor2CCh.retrial_key.mixin.TrialSpawnerAccessor;
import com.Minor2CCh.retrial_key.registry.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TrialSpawnerBlock;
import net.minecraft.block.entity.TrialSpawnerBlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.WindChargeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.AdvancedExplosionBehavior;
import net.minecraft.world.explosion.ExplosionBehavior;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

public class UnstableRetrialKeyItem extends RetrialKeyItem{
    public UnstableRetrialKeyItem(Settings settings) {
        super(settings);
    }
    public int UNSTABLE_PROBABILITY = 20;
    private static final ExplosionBehavior EXPLOSION_BEHAVIOR = new AdvancedExplosionBehavior(
            true, false, Optional.of(1.22F), Registries.BLOCK.getEntryList(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity()));
    @Override
    protected void modifyTrialSpawner(ItemUsageContext context, World world, BlockPos blockPos, PlayerEntity playerEntity){
        Random random = new Random();
        if(random.nextInt(100) < UNSTABLE_PROBABILITY){
            int forkID;
                switch(forkID = random.nextInt(8)){
                    case 0:
                        break;
                    case 1:
                        super.modifyTrialSpawner(context, world, blockPos, playerEntity);
                        BlockState blockState1 = world.getBlockState(blockPos);
                        world.setBlockState(blockPos, blockState1.with(TrialSpawnerBlock.OMINOUS, Boolean.TRUE));
                        break;
                    case 2:
                        super.modifyTrialSpawner(context, world, blockPos, playerEntity);
                        playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 200, 0, false, false, true), null);
                        playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 200, 0, false, false, true), null);
                        break;
                    case 3:
                        super.modifyTrialSpawner(context, world, blockPos, playerEntity);
                        int dropPotato = random.nextInt(2, Items.POISONOUS_POTATO.getMaxCount());
                        for(int i=0;i < dropPotato;i++){
                            Block.dropStack(world, blockPos, Items.POISONOUS_POTATO.getDefaultStack());
                        }
                        break;
                    case 4:
                        super.modifyTrialSpawner(context, world, blockPos, playerEntity);
                        playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 1, false, false, true), null);
                        playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 3, false, false, true), null);
                        break;
                    case 5:
                        super.modifyTrialSpawner(context, world, blockPos, playerEntity);
                        TrialSpawnerBlockEntity trialSpawnerBlockEntity = (TrialSpawnerBlockEntity) world.getBlockEntity(blockPos);
                        ((TrialSpawnerAccessor)(Objects.requireNonNull(trialSpawnerBlockEntity).getSpawner().getData())).setTotalSpawnedMobs(-6);
                        if(playerEntity.hasStatusEffect(StatusEffects.TRIAL_OMEN) || playerEntity.hasStatusEffect(StatusEffects.BAD_OMEN)){
                            BlockState blockState5 = world.getBlockState(blockPos);
                            world.setBlockState(blockPos, blockState5.with(TrialSpawnerBlock.OMINOUS, Boolean.TRUE));
                        }
                        break;
                    case 6:
                        super.modifyTrialSpawner(context, world, blockPos, playerEntity);
                        WindChargeEntity windChargeEntity = new WindChargeEntity(world, context.getHitPos().getX(), context.getHitPos().getY(), context.getHitPos().getZ(), new Vec3d(0, -0.125, 0));
                            world.createExplosion(
                                    windChargeEntity,
                                    null,
                                    EXPLOSION_BEHAVIOR,
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
                        super.modifyTrialSpawner(context, world, blockPos, playerEntity);
                        for (int i = 0; i < 16; i++) {
                            double d = playerEntity.getX() + (playerEntity.getRandom().nextDouble() - 0.5) * 16.0;
                            double e = MathHelper.clamp(
                                    playerEntity.getY() + (playerEntity.getRandom().nextInt(16) - 8), (double)world.getBottomY(), (double)(world.getBottomY() + ((ServerWorld)world).getLogicalHeight() - 1)
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
                        playerEntity.getItemCooldownManager().set(this, 20);
                        break;
                }
                if(!world.isClient()){
                    playerEntity.sendMessage(Text.translatable(ModItems.UNSTABLE_RETRIAL_KEY.get().getTranslationKey()+".fail."+forkID), true);
                }
        }else{
            super.modifyTrialSpawner(context, world, blockPos, playerEntity);
        }
    }
    @Override
    public void appendTooltip(ItemStack itemStack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        //tooltip.add(Text.translatable("item.minium_me.energy.type", Text.translatable(energyName)).formatted(formatting));
        tooltip.add(Text.translatable(itemStack.getTranslationKey()+".desc", UNSTABLE_PROBABILITY).formatted(Formatting.WHITE));


    }
}
