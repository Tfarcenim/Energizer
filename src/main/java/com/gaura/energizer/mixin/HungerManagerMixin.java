package com.gaura.energizer.mixin;

import com.gaura.energizer.Energizer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public class HungerManagerMixin {

    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    public void update(PlayerEntity player, CallbackInfo ci) {

        if (!FabricLoader.getInstance().isModLoaded(Energizer.HEARTY_MEALS_MOD_ID)) {

            ci.cancel();
        }
    }

    @Inject(method = "addExhaustion", at = @At("HEAD"), cancellable = true)
    public void addExhaustion(float exhaustion, CallbackInfo ci) {

        if (!FabricLoader.getInstance().isModLoaded(Energizer.HEARTY_MEALS_MOD_ID)) {

            ci.cancel();
        }
    }
}
