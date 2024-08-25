package com.gaura.energizer.mixin;

import com.gaura.energizer.Energizer;
import com.gaura.energizer.utils.IPlayerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Inject(method = "canSprint", at = @At("RETURN"), cancellable = true)
    private void canSprint(CallbackInfoReturnable<Boolean> cir) {

        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player != null) {

            boolean stopSprint = ((IPlayerEntity) client.player).getStopSprint().getBoolean("stopSprint");

            float currentStamina = client.player.getDataTracker().get(Energizer.STAMINA_DATA);

            if (Energizer.CONFIG.can_continue_sprinting && (!stopSprint && (MinecraftClient.getInstance().options.sprintKey.isPressed() && Energizer.CONFIG.sprint_keybind))) {

                cir.setReturnValue(true);
            }
            else if ((stopSprint || (!MinecraftClient.getInstance().options.sprintKey.isPressed() && Energizer.CONFIG.sprint_keybind) || (currentStamina <= 0.0F && !Energizer.CONFIG.disable_sprint_swim_empty_stamina)) && !client.player.isCreative() && !client.player.isSpectator() && client.interactionManager != null && !(client.player.getWorld().getDifficulty() == Difficulty.PEACEFUL && Energizer.CONFIG.disable_stamina_in_peaceful)) {

                cir.setReturnValue(false);
            }
        }
    }
}
