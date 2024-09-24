package com.gaura.energizer.mixin;

import com.gaura.energizer.Energizer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixinFabric {

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void updateStamina(CallbackInfo ci) {
        Energizer.playerTick((Player) (Object)this);
    }
}
