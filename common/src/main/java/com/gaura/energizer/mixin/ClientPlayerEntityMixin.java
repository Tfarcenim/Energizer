package com.gaura.energizer.mixin;

import com.gaura.energizer.Huds;
import com.gaura.energizer.IPlayerEntity;
import com.gaura.energizer.platform.Services;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class ClientPlayerEntityMixin {

    @Inject(method = "canStartSprinting", at = @At("RETURN"), cancellable = true)
    private void canSprint(CallbackInfoReturnable<Boolean> cir) {
        Boolean allowSprinting = Huds.shouldAllowSprinting((LocalPlayer) (Object)this);
        if (allowSprinting != null) {
            cir.setReturnValue(allowSprinting);
        }
    }

    @Inject(method = "hasEnoughFoodToStartSprinting",at = @At("RETURN"),cancellable = true)
    private void checkStamina(CallbackInfoReturnable<Boolean> cir) {
        IPlayerEntity iPlayerEntity = (IPlayerEntity) this;
        float currentStamina = iPlayerEntity.getStamina();
        if (currentStamina <= 0.0F && Services.PLATFORM.getConfig().disableSprintSwimEmptyStamina()) {
            cir.setReturnValue(false);
        }
    }
}
