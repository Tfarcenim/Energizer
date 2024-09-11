package com.gaura.energizer.mixin;

import com.gaura.energizer.EnergizerFabric;
import com.gaura.energizer.init.ModObjects;
import com.gaura.energizer.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Gui.class)
public class InGameHudMixinHungerAndAir {
    
    private static final String method = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V";

    @Redirect(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = method, ordinal = 3))
    private void removeHungerBarFirst(GuiGraphics context, ResourceLocation texture, int x, int y, int u, int v, int width, int height) {

        if (!EnergizerFabric.CONFIG.remove_hunger) {

            context.blit(texture, x + EnergizerFabric.CONFIG.x_offset_hunger_bar, y - EnergizerFabric.CONFIG.y_offset_hunger_bar, u, v, width, height);
        }
    }

    @Redirect(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = method, ordinal = 4))
    private void removeHungerBarSecond(GuiGraphics context, ResourceLocation texture, int x, int y, int u, int v, int width, int height) {

        if (!EnergizerFabric.CONFIG.remove_hunger) {

            context.blit(texture, x + EnergizerFabric.CONFIG.x_offset_hunger_bar, y - EnergizerFabric.CONFIG.y_offset_hunger_bar, u, v, width, height);
        }
    }

    @Redirect(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = method, ordinal = 5))
    private void removeHungerBarThird(GuiGraphics context, ResourceLocation texture, int x, int y, int u, int v, int width, int height) {

        if (!EnergizerFabric.CONFIG.remove_hunger) {

            context.blit(texture, x + EnergizerFabric.CONFIG.x_offset_hunger_bar, y - EnergizerFabric.CONFIG.y_offset_hunger_bar, u, v, width, height);
        }
    }

    @Redirect(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = method, ordinal = 6))
    private void adjustAirBarFirst(GuiGraphics context, ResourceLocation texture, int x, int y, int u, int v, int width, int height) {

        context.blit(texture, getAirX(x), getAirY(y), u, v, width, height);
    }

    @Redirect(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = method, ordinal = 7))
    private void adjustAirBarSecond(GuiGraphics context, ResourceLocation texture, int x, int y, int u, int v, int width, int height) {

        context.blit(texture, getAirX(x), getAirY(y), u, v, width, height);
    }

    private int getAirX(int x) {

        return x + EnergizerFabric.CONFIG.x_offset_air_bar + (EnergizerFabric.CONFIG.sync_with_stamina_bar ? EnergizerFabric.CONFIG.x_offset_stamina_bar : 0);
    }

    private int getAirY(int y) {

        Minecraft client = Minecraft.getInstance();

        float maxStamina = (float) client.player.getAttributeValue(ModObjects.STAMINA_ATTRIBUTE);
        int staminaLines = (int) Math.ceil(maxStamina / 20);
        int yDecrement = Utils.getYDecrement(maxStamina);

        return y - EnergizerFabric.CONFIG.y_offset_air_bar + (EnergizerFabric.CONFIG.sync_with_stamina_bar ? - (staminaLines - 1) * yDecrement - EnergizerFabric.CONFIG.y_offset_stamina_bar : 0);
    }
}