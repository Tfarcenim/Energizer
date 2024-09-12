package com.gaura.energizer.mixin;

import com.gaura.energizer.*;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(export = true)
@Mixin(Gui.class)
public abstract class InGameHudMixin {
    @Inject(method = "renderPlayerHealth", at = @At("TAIL"))
    private void renderStaminaBar(GuiGraphics context, CallbackInfo ci) {
        Huds.renderStamina((Gui)(Object)this,context, context.guiWidth(), context.guiHeight());
    }
}