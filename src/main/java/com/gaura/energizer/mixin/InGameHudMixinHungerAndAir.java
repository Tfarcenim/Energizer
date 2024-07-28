package com.gaura.energizer.mixin;

import com.gaura.energizer.Energizer;
import com.gaura.energizer.utils.MethodeHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InGameHud.class)
public class InGameHudMixinHungerAndAir {

    @Redirect(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", ordinal = 3))
    private void removeHungerBarFirst(DrawContext context, Identifier texture, int x, int y, int u, int v, int width, int height) {

        if (!Energizer.CONFIG.remove_hunger) {

            context.drawTexture(texture, x + Energizer.CONFIG.x_offset_hunger_bar, y - Energizer.CONFIG.y_offset_hunger_bar, u, v, width, height);
        }
    }

    @Redirect(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", ordinal = 4))
    private void removeHungerBarSecond(DrawContext context, Identifier texture, int x, int y, int u, int v, int width, int height) {

        if (!Energizer.CONFIG.remove_hunger) {

            context.drawTexture(texture, x + Energizer.CONFIG.x_offset_hunger_bar, y - Energizer.CONFIG.y_offset_hunger_bar, u, v, width, height);
        }
    }

    @Redirect(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", ordinal = 5))
    private void removeHungerBarThird(DrawContext context, Identifier texture, int x, int y, int u, int v, int width, int height) {

        if (!Energizer.CONFIG.remove_hunger) {

            context.drawTexture(texture, x + Energizer.CONFIG.x_offset_hunger_bar, y - Energizer.CONFIG.y_offset_hunger_bar, u, v, width, height);
        }
    }

    @Redirect(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", ordinal = 6))
    private void adjustAirBarFirst(DrawContext context, Identifier texture, int x, int y, int u, int v, int width, int height) {

        context.drawTexture(texture, getAirX(x), getAirY(y), u, v, width, height);
    }

    @Redirect(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", ordinal = 7))
    private void adjustAirBarSecond(DrawContext context, Identifier texture, int x, int y, int u, int v, int width, int height) {

        context.drawTexture(texture, getAirX(x), getAirY(y), u, v, width, height);
    }

    private int getAirX(int x) {

        return x + Energizer.CONFIG.x_offset_air_bar + (Energizer.CONFIG.sync_with_stamina_bar ? Energizer.CONFIG.x_offset_stamina_bar : 0);
    }

    private int getAirY(int y) {

        MinecraftClient client = MinecraftClient.getInstance();

        float maxStamina = (float) client.player.getAttributeValue(Energizer.STAMINA_ATTRIBUTE);
        int staminaLines = (int) Math.ceil(maxStamina / 20);
        int yDecrement = MethodeHelper.getYDecrement(maxStamina);

        return y - Energizer.CONFIG.y_offset_air_bar + (Energizer.CONFIG.sync_with_stamina_bar ? - (staminaLines - 1) * yDecrement - Energizer.CONFIG.y_offset_stamina_bar : 0);
    }
}