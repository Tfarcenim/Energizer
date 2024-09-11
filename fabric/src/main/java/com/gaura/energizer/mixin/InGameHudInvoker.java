package com.gaura.energizer.mixin;

import net.minecraft.client.gui.Gui;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Gui.class)
public interface InGameHudInvoker {

    @Invoker("getPlayerVehicleWithHealth")
    LivingEntity invokeGetRiddenEntity();

    @Invoker("getVehicleMaxHearts")
    int invokeGetHeartCount(LivingEntity entity);
}
