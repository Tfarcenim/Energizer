package com.gaura.energizer.mixin;

import com.gaura.energizer.Energizer;
import com.gaura.energizer.Huds;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

//@Debug(export = true)
@Mixin(Gui.class)
public abstract class InGameHudMixinHearts {

    @ModifyVariable(method = "renderHearts", at = @At(value = "LOAD",ordinal = 1),ordinal = 4)
    private int fixedRenderHealthBar(int value, GuiGraphics context, Player player, int x, int y, int lines, int regeneratingHeartIndex,
                                     float maxHealth, int lastHealth, int health, int absorption, boolean blinking,
                                     @Local Gui.HeartType heartType,@Local(ordinal = 7) int i,@Local(ordinal = 8) int j,
                                     @Local(ordinal = 9) int k, @Local(ordinal = 10) int l,@Local(ordinal = 11) int m,
                                     @Local(ordinal = 12) int n,@Local(ordinal = 13) int o,@Local(ordinal = 14) int p,
                                     @Local(ordinal = 15) int q, @Local(ordinal = 16) int r) {
        if (Energizer.removeHunger()) {
            Huds.renderHealed(context, player, x, y, lines, regeneratingHeartIndex, maxHealth, lastHealth, health, absorption, blinking, heartType, i, j, k, l, m, n, o, p, q, r);
        }
        return value;
    }
}