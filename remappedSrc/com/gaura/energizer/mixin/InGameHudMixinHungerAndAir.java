package com.gaura.energizer.mixin;

import com.gaura.energizer.Energizer;
import com.gaura.energizer.utils.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public class InGameHudMixinHungerAndAir {

    @Redirect(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", ordinal = 3))
    private void removeHungerBarFirst(GuiGraphics context, ResourceLocation texture, int x, int y, int u, int v, int width, int height) {

        if (!Energizer.CONFIG.remove_hunger) {

            context.blit(texture, x + Energizer.CONFIG.x_offset_hunger_bar, y - Energizer.CONFIG.y_offset_hunger_bar, u, v, width, height);
        }
    }

    @Redirect(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", ordinal = 4))
    private void removeHungerBarSecond(GuiGraphics context, ResourceLocation texture, int x, int y, int u, int v, int width, int height) {

        if (!Energizer.CONFIG.remove_hunger) {

            context.blit(texture, x + Energizer.CONFIG.x_offset_hunger_bar, y - Energizer.CONFIG.y_offset_hunger_bar, u, v, width, height);
        }
    }

    @Redirect(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", ordinal = 5))
    private void removeHungerBarThird(GuiGraphics context, ResourceLocation texture, int x, int y, int u, int v, int width, int height) {

        if (!Energizer.CONFIG.remove_hunger) {

            context.blit(texture, x + Energizer.CONFIG.x_offset_hunger_bar, y - Energizer.CONFIG.y_offset_hunger_bar, u, v, width, height);
        }
    }

    @Redirect(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", ordinal = 6))
    private void adjustAirBarFirst(GuiGraphics context, ResourceLocation texture, int x, int y, int u, int v, int width, int height) {

        context.blit(texture, getAirX(x), getAirY(y), u, v, width, height);
    }

    @Redirect(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", ordinal = 7))
    private void adjustAirBarSecond(GuiGraphics context, ResourceLocation texture, int x, int y, int u, int v, int width, int height) {

        context.blit(texture, getAirX(x), getAirY(y), u, v, width, height);
    }

    private int getAirX(int x) {

        return x + Energizer.CONFIG.x_offset_air_bar + (Energizer.CONFIG.sync_with_stamina_bar ? Energizer.CONFIG.x_offset_stamina_bar : 0);
    }

    private int getAirY(int y) {

        Minecraft client = Minecraft.getInstance();

        float maxStamina = (float) client.player.getAttributeValue(Energizer.STAMINA_ATTRIBUTE);
        int staminaLines = (int) Math.ceil(maxStamina / 20);
        int yDecrement = Utils.getYDecrement(maxStamina);

        return y - Energizer.CONFIG.y_offset_air_bar + (Energizer.CONFIG.sync_with_stamina_bar ? - (staminaLines - 1) * yDecrement - Energizer.CONFIG.y_offset_stamina_bar : 0);
    }
}