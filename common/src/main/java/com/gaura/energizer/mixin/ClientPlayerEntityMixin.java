package com.gaura.energizer.mixin;

import com.gaura.energizer.Huds;
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
}
