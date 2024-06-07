package com.gaura.energizer.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(InGameHud.class)
public interface InGameHudInvoker {

    @Invoker("getRiddenEntity")
    LivingEntity invokeGetRiddenEntity();

    @Invoker("getHeartCount")
    int invokeGetHeartCount(LivingEntity entity);
}
