package com.gaura.energizer.mixin;

import com.gaura.energizer.EnergizerFabric;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public class HungerManagerMixin {

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void update(Player player, CallbackInfo ci) {

        if (!FabricLoader.getInstance().isModLoaded(EnergizerFabric.HEARTY_MEALS_MOD_ID) && EnergizerFabric.CONFIG.remove_hunger) {

            ci.cancel();
        }
    }

    @Inject(method = "addExhaustion", at = @At("HEAD"), cancellable = true)
    public void addExhaustion(float exhaustion, CallbackInfo ci) {

        if (!FabricLoader.getInstance().isModLoaded(EnergizerFabric.HEARTY_MEALS_MOD_ID) && EnergizerFabric.CONFIG.remove_hunger) {

            ci.cancel();
        }
    }
}
