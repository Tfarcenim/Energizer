package com.gaura.energizer.mixin;

import com.gaura.energizer.Energizer;
import com.gaura.energizer.utils.IPlayerEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "getJumpVelocityMultiplier", at = @At("RETURN"), cancellable = true)
    private void modifyJumpHeight(CallbackInfoReturnable<Float> cir) {

        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player != null && !client.player.isCreative() && !client.player.isSpectator() && Energizer.CONFIG.lower_jump && client.interactionManager != null && !(client.player.getWorld().getDifficulty() == Difficulty.PEACEFUL && Energizer.CONFIG.disable_stamina_in_peaceful)) {

            if (client.player != null && ((IPlayerEntity) client.player).getStopSprint().getBoolean("stopSprint")) {

                cir.setReturnValue(cir.getReturnValue() * Energizer.CONFIG.lower_jump_multiplier);
            }
        }
    }

    @Inject(method = "getVelocityMultiplier", at = @At("RETURN"), cancellable = true)
    private void modifyWalkSpeed(CallbackInfoReturnable<Float> cir) {

        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player != null && !client.player.isCreative() && !client.player.isSpectator() && Energizer.CONFIG.slower_walk && client.interactionManager != null && !(client.player.getWorld().getDifficulty() == Difficulty.PEACEFUL && Energizer.CONFIG.disable_stamina_in_peaceful)) {

            if (client.player != null && ((IPlayerEntity) client.player).getStopSprint().getBoolean("stopSprint")) {

                cir.setReturnValue(cir.getReturnValue() * Energizer.CONFIG.slower_walk_multiplier);
            }
        }
    }
}
