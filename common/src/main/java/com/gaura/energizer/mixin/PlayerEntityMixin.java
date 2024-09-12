package com.gaura.energizer.mixin;

import com.gaura.energizer.Energizer;
import com.gaura.energizer.IPlayerEntity;
import com.gaura.energizer.init.ModObjects;
import com.gaura.energizer.network.S2CSetStaminaPacket;
import com.gaura.energizer.platform.Services;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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

    @Unique
    private boolean stopSprint;

    @Unique
    private long lastStaminaLossTime = 0;

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
            cir.setReturnValue(Energizer.onEaten((Player)(Object)this,world,stack));
        }
    }

    @Inject(method = "createAttributes", at = @At("RETURN"))
    private static void addStaminaAttribute(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.getReturnValue().add(ModObjects.STAMINA_ATTRIBUTE);
    }

    @Override
    public long getLastStaminaLossTime() {
        return lastStaminaLossTime;
    }

    @Override
    public void setLastStaminaLossTIme(long time) {
        lastStaminaLossTime = time;
    }


    @Inject(method = "aiStep", at = @At("HEAD"))
    private void updateStamina(CallbackInfo ci) {
        Energizer.playerTick((Player) (Object)this);
    }

    @Override
    public float getStamina() {
        return stamina;
    }

    @Override
    public void setStamina(float stamina) {
        this.stamina = stamina;
        if ((Object) this instanceof ServerPlayer) {
            Services.PLATFORM.sendToClient(new S2CSetStaminaPacket(stamina),(ServerPlayer) (Object) this);
        }
    }

    @Override
    public float getMaxStamina() {
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