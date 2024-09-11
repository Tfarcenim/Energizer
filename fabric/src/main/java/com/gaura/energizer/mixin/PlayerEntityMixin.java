package com.gaura.energizer.mixin;

import com.gaura.energizer.Energizer;
import com.gaura.energizer.EnergizerFabric;
import com.gaura.energizer.IPlayerEntity;
import com.gaura.energizer.init.ModObjects;
import com.gaura.energizer.network.S2CSetStaminaPacket;
import com.gaura.energizer.network.S2CStopSprintPacket;
import com.gaura.energizer.platform.Services;
import com.gaura.energizer.utils.Utils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerEntityMixin implements IPlayerEntity {

    @Unique
    float stamina;

    @Inject(method = "canEat", at = @At("RETURN"), cancellable = true)
    public void canConsume(boolean ignoreHunger, CallbackInfoReturnable<Boolean> cir) {

        if (Energizer.removeHunger()) {

            Player player = (Player) (Object) this;

            if (!player.isCreative()) {

                cir.setReturnValue(!(player.getHealth() == player.getMaxHealth()) || ignoreHunger);
            }
        }
    }

    @Inject(method = "eat", at = @At("HEAD"), cancellable = true)
    public void eatFood(Level world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {

        if (Energizer.removeHunger()) {

            Player player = (Player) (Object) this;

            if (stack.isEdible() && player instanceof ServerPlayer serverPlayer && !world.isClientSide()) {

                serverPlayer.awardStat(Stats.ITEM_USED.get(stack.getItem()));

                world.playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);

                ((LivingEntityInvoker) serverPlayer).invokeApplyFoodEffects(stack, world, serverPlayer);

                CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);

                serverPlayer.heal(Utils.getHealAmount(stack));

                if (!serverPlayer.isCreative()) {

                    stack.shrink(1);
                }

                serverPlayer.gameEvent(GameEvent.EAT);
            }

            cir.setReturnValue(stack);
        }
    }

    @Inject(method = "createAttributes", at = @At("RETURN"))
    private static void addStaminaAttribute(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {

        cir.getReturnValue().add(ModObjects.STAMINA_ATTRIBUTE);
    }

    public boolean stopSprint;

    private long lastStaminaLossTime = 0;

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void updateStamina(CallbackInfo ci) {

        Player player = (Player) (Object) this;
        long currentTime = player.level().getGameTime();

        if (!player.isCreative() && !player.isSpectator() && !player.level().isClientSide && !(player.level().getDifficulty() == Difficulty.PEACEFUL && EnergizerFabric.CONFIG.disable_stamina_in_peaceful)) {

            boolean hasHunger = player.hasEffect(MobEffects.HUNGER);
            float staminaIncrease = (hasHunger && stopSprint) ? EnergizerFabric.CONFIG.stamina_increase_hunger_empty : hasHunger ? EnergizerFabric.CONFIG.stamina_increase_hunger : stopSprint ? EnergizerFabric.CONFIG.stamina_increase_empty : EnergizerFabric.CONFIG.stamina_increase;

            if (player.hasEffect(ModObjects.VIGOR)) {

                this.setStamina(this.getMaxStamina());
                this.stopSprint = false;
            } else {

                if (player.isPassenger()) {

                    if ((this.getStamina() < this.getMaxStamina()) && ((currentTime - lastStaminaLossTime) >= EnergizerFabric.CONFIG.stamina_regeneration_delay)) {

                        this.setStamina(this.getStamina() + staminaIncrease);
                    }
                } else {

                    float staminaDecrease;
                    if (player.isSprinting() && !player.isUnderWater() && !stopSprint) {

                        staminaDecrease = hasHunger ? EnergizerFabric.CONFIG.sprinting_stamina_decrease_hunger : EnergizerFabric.CONFIG.sprinting_stamina_decrease;
                        this.setStamina(this.getStamina() - staminaDecrease);
                        lastStaminaLossTime = currentTime;

                        if (this.getStamina() <= 0.0F && EnergizerFabric.CONFIG.disable_sprint_swim_empty_stamina) {

                            this.stopSprint = true;
                        }
                    } else if (player.isSwimming() && player.isUnderWater() && EnergizerFabric.CONFIG.swimming_cost_stamina && !stopSprint) {

                        staminaDecrease = hasHunger ? EnergizerFabric.CONFIG.swimming_stamina_decrease_hunger : EnergizerFabric.CONFIG.swimming_stamina_decrease;
                        this.setStamina(this.getStamina() - staminaDecrease);
                        lastStaminaLossTime = currentTime;

                        if (this.getStamina() <= 0.0F && EnergizerFabric.CONFIG.disable_sprint_swim_empty_stamina) {

                            this.stopSprint = true;
                        }
                    } else if ((this.getStamina() < this.getMaxStamina()) && ((currentTime - lastStaminaLossTime) >= EnergizerFabric.CONFIG.stamina_regeneration_delay)) {

                        this.setStamina(this.getStamina() + staminaIncrease);
                    } else if (this.getStamina() == this.getMaxStamina()) {

                        this.stopSprint = false;
                    }
                }
            }
            Services.PLATFORM.sendToClient(new S2CStopSprintPacket(stopSprint),(ServerPlayer) (Object) this);
            Services.PLATFORM.sendToClient(new S2CSetStaminaPacket(stamina),(ServerPlayer) (Object) this);
        }
    }

    public float getStamina() {
        return stamina;
    }

    public void setStamina(float stamina) {
        this.stamina = stamina;
        if ((Object) this instanceof ServerPlayer) {
            Services.PLATFORM.sendToClient(new S2CSetStaminaPacket(stamina),(ServerPlayer) (Object) this);
        }
    }

    private float getMaxStamina() {

        Player player = (Player) (Object) this;

        return (float) player.getAttributeValue(ModObjects.STAMINA_ATTRIBUTE);
    }

    @Override
    public boolean getStopSprint(){
        return stopSprint;
    }

    @Override
    public void setStopSprint(boolean stopSprint) {
        this.stopSprint = stopSprint;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void writeCustomDataToNbt(CompoundTag nbt, CallbackInfo ci) {

        nbt.putBoolean("StopSprint", this.stopSprint);
        nbt.putFloat("Stamina", this.getStamina());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readCustomDataFromNbt(CompoundTag nbt, CallbackInfo ci) {

        if (nbt.contains("Stamina", Tag.TAG_FLOAT)) {
            this.setStamina(nbt.getFloat("Stamina"));
        }
        if (nbt.contains("StopSprint")) {
            this.stopSprint = nbt.getBoolean("StopSprint");
        }
    }
}