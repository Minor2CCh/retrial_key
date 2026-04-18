package com.Minor2CCh.retrial_key.item;


import com.Minor2CCh.retrial_key.config.ModConfigLoader;
import com.Minor2CCh.retrial_key.mixin.TrialSpawnerAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.TrialSpawnerBlock;
import net.minecraft.world.level.block.entity.TrialSpawnerBlockEntity;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerState;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;

import java.util.Random;

public class RetrialKeyItem extends Item {
    public static final DispenseItemBehavior DISPENSER_BEHAVIOR = new DefaultDispenseItemBehavior()

    {
        @Override
        protected @NonNull ItemStack execute(final @NonNull BlockSource source, final @NonNull ItemStack dispensed) {
            dispenseKey(source, dispensed);
            return dispensed;
        }
    };
    public RetrialKeyItem(Item.Properties settings) {
        super(settings);
        DispenserBlock.registerBehavior(this, DISPENSER_BEHAVIOR);
    }

    public static void dispenseKey(BlockSource source, ItemStack stack) {
        ServerLevel level = source.level();
        BlockPos pos = source.pos();
        if (!level.isClientSide()) {
            Direction dir = source.state().getValue(DispenserBlock.FACING);
            BlockPos frontPos = pos.relative(dir);
            if(level.getBlockState(frontPos).getBlock() instanceof TrialSpawnerBlock) {
                if(stack.getItem() instanceof RetrialKeyItem keyItem) {
                    keyItem.accelerateFromDispenser(source, stack);
                }
            }
        }
    }
    private void accelerateFromDispenser(BlockSource source, ItemStack stack){
        ServerLevel level = source.level();
        Direction dir = source.state().getValue(DispenserBlock.FACING);
        BlockPos pos = source.pos().relative(dir);
        BlockState blockState = level.getBlockState(pos);
        if (blockState.getBlock() instanceof TrialSpawnerBlock) {
            if(blockState.getValue(TrialSpawnerBlock.STATE).equals(TrialSpawnerState.COOLDOWN)){
                if(!level.isClientSide()){
                    modifyFromDispenser(source, false);
                }
                level.playSound(null, pos, SoundEvents.VAULT_INSERT_ITEM_FAIL, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.playSound(null, pos, SoundEvents.TRIAL_SPAWNER_OMINOUS_ACTIVATE, SoundSource.BLOCKS, 1.0F, 0.5F);
                Random random = new Random();
                for(int i=0;i<8;i++){
                    level.sendParticles(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER, pos.getX()+random.nextDouble(-0.25, 1.25), pos.getY()+random.nextDouble(-0.25, 1.25), pos.getZ()+random.nextDouble(-0.25, 1.25), 0, 0, 1, 0, random.nextDouble(0.05, 0.075));
                    level.sendParticles(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS, pos.getX()+random.nextDouble(-0.25, 1.25), pos.getY()+random.nextDouble(-0.25, 1.25), pos.getZ()+random.nextDouble(-0.25, 1.25), 0, 0, 1, 0, random.nextDouble(0.05, 0.075));
                }
                consumeItem(stack, level);
            }
        }
    }
    protected void modifyFromDispenser(BlockSource source, boolean enforcementSkip){
        ServerLevel level = source.level();
        Direction dir = source.state().getValue(DispenserBlock.FACING);
        BlockPos pos = source.pos().relative(dir);
        BlockState blockState = level.getBlockState(pos);
        TrialSpawnerBlockEntity trialSpawnerBlockEntity = (TrialSpawnerBlockEntity) level.getBlockEntity(pos);
        if(trialSpawnerBlockEntity == null){
            return;
        }
        long oldCooldown = ((TrialSpawnerAccessor)(trialSpawnerBlockEntity.getTrialSpawner().getStateData())).cooldownEnd();//クライアントは0のみを返す
        long remainCooldown = oldCooldown - level.getGameTime();
        if(remainCooldown > 20) {
            long newCooldown;
            long skipTime = ModConfigLoader.getConfig().getSkipCooldownTime();
            if(skipTime <= 0 || remainCooldown - skipTime <= 0 || enforcementSkip){
                newCooldown = 0;
            }else{
                newCooldown = oldCooldown - skipTime;
            }
            ((TrialSpawnerAccessor)(trialSpawnerBlockEntity.getTrialSpawner().getStateData())).setCooldownEnd(newCooldown);
            if(newCooldown == 0){
                level.setBlockAndUpdate(pos, blockState.setValue(TrialSpawnerBlock.STATE, TrialSpawnerState.WAITING_FOR_PLAYERS).setValue(TrialSpawnerBlock.OMINOUS, Boolean.FALSE));
            }
        }

    }
    @Override
    public @NonNull InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        Player playerEntity = context.getPlayer();
        if (accelerateTrialSpawner(context, world, blockPos, playerEntity)) {
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
    private boolean accelerateTrialSpawner(UseOnContext context, Level level, BlockPos blockPos, Player player){
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.getBlock() instanceof TrialSpawnerBlock) {
            if(blockState.getValue(TrialSpawnerBlock.STATE).equals(TrialSpawnerState.COOLDOWN)){
                if(!level.isClientSide()){
                    modifyTrialSpawner(context, level, blockPos, player, false);
                }
                level.playSound(player, blockPos, SoundEvents.VAULT_INSERT_ITEM_FAIL, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.playSound(player, blockPos, SoundEvents.TRIAL_SPAWNER_OMINOUS_ACTIVATE, SoundSource.BLOCKS, 1.0F, 0.5F);
                Random random = new Random();
                for(int i=0;i<8;i++){
                    level.addParticle(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER, blockPos.getX()+random.nextDouble(-0.25, 1.25), blockPos.getY()+random.nextDouble(-0.25, 1.25), blockPos.getZ()+random.nextDouble(-0.25, 1.25), 0.0, random.nextDouble(0.05, 0.075), 0.0);
                    level.addParticle(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS, blockPos.getX()+random.nextDouble(-0.25, 1.25), blockPos.getY()+random.nextDouble(-0.25, 1.25), blockPos.getZ()+random.nextDouble(-0.25, 1.25), 0.0, random.nextDouble(0.05, 0.075), 0.0);
                }
                consumeItem(context, player);
                return true;
            }

        }
        return false;
    }
    protected void modifyTrialSpawner(UseOnContext context, Level level, BlockPos blockPos, Player player, boolean enforcementSkip){
        BlockState blockState = level.getBlockState(blockPos);
        TrialSpawnerBlockEntity trialSpawnerBlockEntity = (TrialSpawnerBlockEntity) level.getBlockEntity(blockPos);
        if(trialSpawnerBlockEntity == null){
            return;
        }
        long oldCooldown = ((TrialSpawnerAccessor)(trialSpawnerBlockEntity.getTrialSpawner().getStateData())).cooldownEnd();//クライアントは0のみを返す
        long remainCooldown = oldCooldown - level.getGameTime();
        if(remainCooldown > 20) {
            long newCooldown;
            long skipTime = ModConfigLoader.getConfig().getSkipCooldownTime();
            if(skipTime <= 0 || remainCooldown - skipTime <= 0 || enforcementSkip){
                newCooldown = 0;
            }else{
                newCooldown = oldCooldown - skipTime;
            }
            ((TrialSpawnerAccessor)(trialSpawnerBlockEntity.getTrialSpawner().getStateData())).setCooldownEnd(newCooldown);
            if(newCooldown == 0){
                level.setBlockAndUpdate(blockPos, blockState.setValue(TrialSpawnerBlock.STATE, TrialSpawnerState.WAITING_FOR_PLAYERS).setValue(TrialSpawnerBlock.OMINOUS, Boolean.FALSE));
            }
        }
    }
    protected void consumeItem(UseOnContext context, Player player){
        if(context.getItemInHand().getMaxStackSize() > 1){
            context.getItemInHand().consume(1, player);
        }else if(context.getItemInHand().isDamageableItem()){
            context.getItemInHand().hurtAndBreak(1, player, context.getHand().asEquipmentSlot());
        }
        player.getCooldowns().addCooldown(this.getDefaultInstance(), 30);
    }
    protected void consumeItem(ItemStack stack, Level level){
        if(stack.getMaxStackSize() > 1){
            stack.shrink(1);
        }else if(stack.isDamageableItem() && level instanceof ServerLevel serverLevel){
            stack.hurtAndBreak(1, serverLevel, null, (_) -> {});
        }
    }
}
