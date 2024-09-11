package com.gaura.energizer.mixin;

import com.gaura.energizer.Energizer;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    
    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(CallbackInfo info) {
        
        Energizer.LOG.info("This line is printed by an example mod common mixin!");
        Energizer.LOG.info("MC Version: {}", Minecraft.getInstance().getVersionType());
    }
}