package com.gaura.energizer.mixin;

import com.gaura.energizer.IPlayerEntity;
import com.gaura.energizer.platform.MLConfig;
import com.gaura.energizer.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "getBlockJumpFactor", at = @At("RETURN"), cancellable = true)
    private void modifyJumpHeight(CallbackInfoReturnable<Float> cir) {

        Minecraft client = Minecraft.getInstance();
        MLConfig config = Services.PLATFORM.getConfig();
        if (client.player != null && !client.player.isCreative() && !client.player.isSpectator() && config.lowerJump() && client.gameMode != null && !(client.player.level().getDifficulty() == Difficulty.PEACEFUL && config.disableStaminaInPeaceful())) {

            if (client.player != null && ((IPlayerEntity) client.player).getStopSprint()) {

                cir.setReturnValue((float) (cir.getReturnValue() * config.lowerJumpMultiplier()));
            }
        }
    }

    @Inject(method = "getBlockSpeedFactor", at = @At("RETURN"), cancellable = true)
    private void modifyWalkSpeed(CallbackInfoReturnable<Float> cir) {

        Minecraft client = Minecraft.getInstance();
        MLConfig config = Services.PLATFORM.getConfig();
        if (client.player != null && !client.player.isCreative() && !client.player.isSpectator() && config.slowerWalk() && client.gameMode != null && !(client.player.level().getDifficulty() == Difficulty.PEACEFUL && config.disableStaminaInPeaceful())) {

            if (client.player != null && ((IPlayerEntity) client.player).getStopSprint()) {

                cir.setReturnValue((float) (cir.getReturnValue() * config.slowerWalkMultiplier()));
            }
        }
    }
}
