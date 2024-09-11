package com.gaura.energizer.mixin;

import com.gaura.energizer.EnergizerFabric;
import com.gaura.energizer.utils.IPlayerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(LocalPlayer.class)
public class ClientPlayerEntityMixin {

    @Inject(method = "canStartSprinting", at = @At("RETURN"), cancellable = true)
    private void canSprint(CallbackInfoReturnable<Boolean> cir) {

        Minecraft client = Minecraft.getInstance();

        if (client.player != null) {

            boolean stopSprint = ((IPlayerEntity) client.player).getStopSprint().getBoolean("stopSprint");

            float currentStamina = client.player.getEntityData().get(EnergizerFabric.STAMINA_DATA);

            if (EnergizerFabric.CONFIG.can_continue_sprinting && (!stopSprint && (Minecraft.getInstance().options.keySprint.isDown() && EnergizerFabric.CONFIG.sprint_keybind))) {

                cir.setReturnValue(true);
            }
            else if ((stopSprint || (!Minecraft.getInstance().options.keySprint.isDown() && EnergizerFabric.CONFIG.sprint_keybind) || (currentStamina <= 0.0F && !EnergizerFabric.CONFIG.disable_sprint_swim_empty_stamina)) && !client.player.isCreative() && !client.player.isSpectator() && client.gameMode != null && !(client.player.level().getDifficulty() == Difficulty.PEACEFUL && EnergizerFabric.CONFIG.disable_stamina_in_peaceful)) {

                cir.setReturnValue(false);
            }
        }
    }
}
