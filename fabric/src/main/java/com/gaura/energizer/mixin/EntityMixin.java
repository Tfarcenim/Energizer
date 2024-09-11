package com.gaura.energizer.mixin;

import com.gaura.energizer.EnergizerFabric;
import com.gaura.energizer.IPlayerEntity;
import com.gaura.energizer.platform.Services;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "getBlockJumpFactor", at = @At("RETURN"), cancellable = true)
    private void modifyJumpHeight(CallbackInfoReturnable<Float> cir) {

        Minecraft client = Minecraft.getInstance();

        if (client.player != null && !client.player.isCreative() && !client.player.isSpectator() && Services.PLATFORM.getConfig().lowerJump() && client.gameMode != null && !(client.player.level().getDifficulty() == Difficulty.PEACEFUL && EnergizerFabric.CONFIG.disable_stamina_in_peaceful)) {

            if (client.player != null && ((IPlayerEntity) client.player).getStopSprint()) {

                cir.setReturnValue(cir.getReturnValue() * EnergizerFabric.CONFIG.lower_jump_multiplier);
            }
        }
    }

    @Inject(method = "getBlockSpeedFactor", at = @At("RETURN"), cancellable = true)
    private void modifyWalkSpeed(CallbackInfoReturnable<Float> cir) {

        Minecraft client = Minecraft.getInstance();

        if (client.player != null && !client.player.isCreative() && !client.player.isSpectator() && EnergizerFabric.CONFIG.slower_walk && client.gameMode != null && !(client.player.level().getDifficulty() == Difficulty.PEACEFUL && EnergizerFabric.CONFIG.disable_stamina_in_peaceful)) {

            if (client.player != null && ((IPlayerEntity) client.player).getStopSprint()) {

                cir.setReturnValue(cir.getReturnValue() * EnergizerFabric.CONFIG.slower_walk_multiplier);
            }
        }
    }
}
