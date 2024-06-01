package com.gaura.energizer.mixin;

import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(HungerManager.class)
public class HungerManagerMixin {

    @Overwrite
    public void update(PlayerEntity player) {

        // Do nothing to prevent hunger from updating
    }

    @Overwrite
    public void addExhaustion(float exhaustion) {

        // Do nothing to prevent exhaustion from updating
    }
}
