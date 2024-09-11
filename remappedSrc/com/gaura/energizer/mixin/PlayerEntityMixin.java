package com.gaura.energizer.mixin;

import com.gaura.energizer.Energizer;
import com.gaura.energizer.EnergizerClient;
import com.gaura.energizer.utils.IPlayerEntity;
import com.gaura.energizer.utils.Utils;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerEntityMixin implements IPlayerEntity {

    @Shadow @Nullable public FishingHook fishHook;

    @Inject(method = "canConsume", at = @At("RETURN"), cancellable = true)
    public void canConsume(boolean ignoreHunger, CallbackInfoReturnable<Boolean> cir) {

        if (!FabricLoader.getInstance().isModLoaded(Energizer.HEARTY_MEALS_MOD_ID) && Energizer.CONFIG.remove_hunger) {

            Player player = (Player) (Object) this;

            if (!player.isCreative()) {

                cir.setReturnValue(!(player.getHealth() == player.getMaxHealth()) || ignoreHunger);
            }
        }
    }

    @Inject(method = "eatFood", at = @At("HEAD"), cancellable = true)
    public void eatFood(Level world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {

        if (!FabricLoader.getInstance().isModLoaded(Energizer.HEARTY_MEALS_MOD_ID) && Energizer.CONFIG.remove_hunger) {

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

    @Inject(method = "createPlayerAttributes", at = @At("RETURN"))
    private static void addStaminaAttribute(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {

        cir.getReturnValue().add(Energizer.STAMINA_ATTRIBUTE);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    protected void addStaminaDataTracker(CallbackInfo ci) {

        Player player = (Player) (Object) this;

        player.getEntityData().define(Energizer.STAMINA_DATA, 1.0F);
    }

    public boolean stopSprint;

    private long lastStaminaLossTime = 0;

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void updateStamina(CallbackInfo ci) {

        Player player = (Player) (Object) this;
        long currentTime = player.level().getGameTime();

        if (!player.isCreative() && !player.isSpectator() && !player.level().isClientSide && !(player.level().getDifficulty() == Difficulty.PEACEFUL && Energizer.CONFIG.disable_stamina_in_peaceful)) {

            boolean hasHunger = player.hasEffect(MobEffects.HUNGER);
            float staminaDecrease = 0.0F;
            float staminaIncrease = (hasHunger && stopSprint) ? Energizer.CONFIG.stamina_increase_hunger_empty : hasHunger ? Energizer.CONFIG.stamina_increase_hunger : stopSprint ? Energizer.CONFIG.stamina_increase_empty : Energizer.CONFIG.stamina_increase;

            if (player.hasEffect(Energizer.VIGOR)) {

                this.setStamina(this.getMaxStamina());
                this.stopSprint = false;
            }
            else {

                if (player.isPassenger()) {

                    if ((this.getStamina() < this.getMaxStamina()) && ((currentTime - lastStaminaLossTime) >= Energizer.CONFIG.stamina_regeneration_delay)) {

                        this.setStamina(this.getStamina() + staminaIncrease);
                    }
                }
                else {

                    if (player.isSprinting() && !player.isUnderWater() && !stopSprint) {

                        staminaDecrease = hasHunger ? Energizer.CONFIG.sprinting_stamina_decrease_hunger : Energizer.CONFIG.sprinting_stamina_decrease;
                        this.setStamina(this.getStamina() - staminaDecrease);
                        lastStaminaLossTime = currentTime;

                        if (this.getStamina() <= 0.0F && Energizer.CONFIG.disable_sprint_swim_empty_stamina) {

                            this.stopSprint = true;
                        }
                    }
                    else if (player.isSwimming() && player.isUnderWater() && Energizer.CONFIG.swimming_cost_stamina && !stopSprint) {

                        staminaDecrease = hasHunger ? Energizer.CONFIG.swimming_stamina_decrease_hunger : Energizer.CONFIG.swimming_stamina_decrease;
                        this.setStamina(this.getStamina() - staminaDecrease);
                        lastStaminaLossTime = currentTime;

                        if (this.getStamina() <= 0.0F && Energizer.CONFIG.disable_sprint_swim_empty_stamina) {

                            this.stopSprint = true;
                        }
                    }
                    else if ((this.getStamina() < this.getMaxStamina()) && ((currentTime - lastStaminaLossTime) >= Energizer.CONFIG.stamina_regeneration_delay)) {

                        this.setStamina(this.getStamina() + staminaIncrease);
                    }
                    else if (this.getStamina() == this.getMaxStamina()) {

                        this.stopSprint = false;
                    }
                }
            }

            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeBoolean(this.stopSprint);
            ServerPlayNetworking.send((ServerPlayer) player, EnergizerClient.STOP_SPRINT_ID, buf);
        }
    }

    private float getStamina() {

        Player player = (Player) (Object) this;

        return player.getEntityData().get(Energizer.STAMINA_DATA);
    }

    private void setStamina(float stamina) {

        Player player = (Player) (Object) this;

        player.getEntityData().set(Energizer.STAMINA_DATA, Mth.clamp(stamina, 0.0F, this.getMaxStamina()));
    }

    private float getMaxStamina() {

        Player player = (Player) (Object) this;

        return (float) player.getAttributeValue(Energizer.STAMINA_ATTRIBUTE);
    }

    private CompoundTag persistentData;

    @Override
    public CompoundTag getStopSprint() {

        if (this.persistentData == null) {

            this.persistentData = new CompoundTag();
        }

        return persistentData;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeCustomDataToNbt(CompoundTag nbt, CallbackInfo ci) {

        nbt.putBoolean("StopSprint", this.stopSprint);
        nbt.putFloat("Stamina", this.getStamina());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomDataFromNbt(CompoundTag nbt, CallbackInfo ci) {

        if (nbt.contains("Stamina", Tag.TAG_ANY_NUMERIC)) {

            this.setStamina(nbt.getFloat("Stamina"));
        }
        if (nbt.contains("StopSprint")) {

            this.stopSprint = nbt.getBoolean("StopSprint");
        }
    }
}