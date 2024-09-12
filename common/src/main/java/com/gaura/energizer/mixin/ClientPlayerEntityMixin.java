package com.gaura.energizer.mixin;

import com.gaura.energizer.IPlayerEntity;
import com.gaura.energizer.platform.MLConfig;
import com.gaura.energizer.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class ClientPlayerEntityMixin {

    @Inject(method = "canStartSprinting", at = @At("RETURN"), cancellable = true)
    private void canSprint(CallbackInfoReturnable<Boolean> cir) {

        Minecraft client = Minecraft.getInstance();

        if (client.player != null) {

            IPlayerEntity iPlayerEntity = (IPlayerEntity) client.player;

            boolean stopSprint = iPlayerEntity.getStopSprint();

            float currentStamina = iPlayerEntity.getStamina();

            MLConfig config = Services.PLATFORM.getConfig();

            if (config.canContinueSprinting() && (!stopSprint && (Minecraft.getInstance().options.keySprint.isDown() && config.sprintKeybind()))) {

                cir.setReturnValue(true);
            }
            else if ((stopSprint || (!Minecraft.getInstance().options.keySprint.isDown() && config.sprintKeybind()) ||
                    (currentStamina <= 0.0F && !config.disableSprintSwimEmptyStamina())) && !client.player.isCreative() && !client.player.isSpectator() && client.gameMode != null && !(client.player.level().getDifficulty() == Difficulty.PEACEFUL && config.disableStaminaInPeaceful())) {

                cir.setReturnValue(false);
            }
        }
    }
}
