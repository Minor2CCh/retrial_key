package com.Minor2CCh.retrial_key.item;


import com.Minor2CCh.retrial_key.config.ModConfigLoader;
import com.Minor2CCh.retrial_key.mixin.TrialSpawnerAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.TrialSpawnerBlock;
import net.minecraft.block.entity.TrialSpawnerBlockEntity;
import net.minecraft.block.enums.TrialSpawnerState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.Random;

public class RetrialKeyItem extends Item {
    public RetrialKeyItem(Item.Settings settings) {
        super(settings);
    }
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        PlayerEntity playerEntity = context.getPlayer();
        if (accelerateTrialSpawner(context, world, blockPos, playerEntity)) {
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
    private boolean accelerateTrialSpawner(ItemUsageContext context, World world, BlockPos blockPos, PlayerEntity playerEntity){
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.getBlock() instanceof TrialSpawnerBlock) {
            if(blockState.get(TrialSpawnerBlock.TRIAL_SPAWNER_STATE).equals(TrialSpawnerState.COOLDOWN)){
                if(!world.isClient()){
                    modifyTrialSpawner(context, world, blockPos, playerEntity, false);
                }
                world.playSound(playerEntity, blockPos, SoundEvents.BLOCK_VAULT_INSERT_ITEM_FAIL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.playSound(playerEntity, blockPos, SoundEvents.BLOCK_TRIAL_SPAWNER_OMINOUS_ACTIVATE, SoundCategory.BLOCKS, 1.0F, 0.5F);
                Random random = new Random();
                for(int i=0;i<8;i++){
                    world.addParticleClient(ParticleTypes.TRIAL_SPAWNER_DETECTION, blockPos.getX()+random.nextDouble(-0.25, 1.25), blockPos.getY()+random.nextDouble(-0.25, 1.25), blockPos.getZ()+random.nextDouble(-0.25, 1.25), 0.0, random.nextDouble(0.05, 0.075), 0.0);
                    world.addParticleClient(ParticleTypes.TRIAL_SPAWNER_DETECTION_OMINOUS, blockPos.getX()+random.nextDouble(-0.25, 1.25), blockPos.getY()+random.nextDouble(-0.25, 1.25), blockPos.getZ()+random.nextDouble(-0.25, 1.25), 0.0, random.nextDouble(0.05, 0.075), 0.0);
                }
                consumeItem(context, playerEntity);
                return true;
            }

        }
        return false;
    }
    protected void modifyTrialSpawner(ItemUsageContext context, World world, BlockPos blockPos, PlayerEntity playerEntity, boolean enforcementSkip){
        BlockState blockState = world.getBlockState(blockPos);
        TrialSpawnerBlockEntity trialSpawnerBlockEntity = (TrialSpawnerBlockEntity) world.getBlockEntity(blockPos);
        long oldCooldown = ((TrialSpawnerAccessor)(Objects.requireNonNull(trialSpawnerBlockEntity).getSpawner().getData())).cooldownEnd();//クライアントは0のみを返す
        //long cooldownLength = Objects.requireNonNull(trialSpawnerBlockEntity).getSpawner().getCooldownLength();
        long remainCooldown = oldCooldown - world.getTime();
        if(remainCooldown > 20) {
            long newCooldown;
            long skipTime = ModConfigLoader.getConfig().skipCooldownTime;
            if(skipTime <= 0 || remainCooldown - skipTime <= 0 || enforcementSkip){
                newCooldown = 0;
            }else{
                newCooldown = oldCooldown - skipTime;
            }
            ((TrialSpawnerAccessor)(Objects.requireNonNull(trialSpawnerBlockEntity).getSpawner().getData())).setCooldownEnd(newCooldown);
            if(newCooldown == 0){
                world.setBlockState(blockPos, blockState.with(TrialSpawnerBlock.TRIAL_SPAWNER_STATE, TrialSpawnerState.WAITING_FOR_PLAYERS).with(TrialSpawnerBlock.OMINOUS, Boolean.FALSE));
            }
        }
    }
    protected void consumeItem(ItemUsageContext context, PlayerEntity playerEntity){
        if(context.getStack().getItem().getMaxCount() > 1){
            context.getStack().decrementUnlessCreative(1, playerEntity);
        }else if(context.getStack().isDamageable()){
            context.getStack().damage(1, playerEntity, LivingEntity.getSlotForHand(context.getHand()));
        }
        playerEntity.getItemCooldownManager().set(this.getDefaultStack(), 30);
    }/*
    @Override
    public void appendTooltip(ItemStack itemStack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(Text.translatable(itemStack.getItem().getTranslationKey()+".desc", ModItems.KEY_DURABILITY).formatted(Formatting.WHITE));


    }
    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        return Optional.empty();
    }*/

}
