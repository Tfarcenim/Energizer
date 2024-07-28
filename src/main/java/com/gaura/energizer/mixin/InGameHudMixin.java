package com.gaura.energizer.mixin;

import com.gaura.energizer.Energizer;
import com.gaura.energizer.config.HealFood;
import com.gaura.energizer.utils.IPlayerEntity;
import com.gaura.energizer.utils.MethodeHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    private static final Identifier STAMINA_ICONS = new Identifier(Energizer.MOD_ID, "textures/stamina/stamina_icons.png");

    private static final int X_OFFSET = 82;
    private static final int Y_OFFSET = 39;

    private int lastFullFillTime = -1;

    @Inject(method = "renderStatusBars", at = @At("TAIL"))
    private void renderStaminaBar(DrawContext context, CallbackInfo ci) {

        MinecraftClient client = MinecraftClient.getInstance();

        if (((InGameHudInvoker) this).invokeGetHeartCount(((InGameHudInvoker) this).invokeGetRiddenEntity()) == 0) {

            int x = (client.getWindow().getScaledWidth() / 2) + X_OFFSET + Energizer.CONFIG.x_offset_stamina_bar;
            int y = client.getWindow().getScaledHeight() - Y_OFFSET - Energizer.CONFIG.y_offset_stamina_bar;

            int vigorIndex = -1;

            float maxStamina = (float) client.player.getAttributeValue(Energizer.STAMINA_ATTRIBUTE);
            float currentStamina = client.player.getDataTracker().get(Energizer.STAMINA_DATA);

            boolean hasHunger = client.player.hasStatusEffect(StatusEffects.HUNGER);
            boolean hasVigor = client.player.hasStatusEffect(Energizer.VIGOR);
            boolean stopSprint = ((IPlayerEntity) client.player).getStopSprint().getBoolean("stopSprint");

            int lines = (int) Math.ceil(maxStamina / 20);
            int fullIconsPerLine = (int) (currentStamina / 2);
            boolean halfIcon = Math.floor(currentStamina) % 2 != 0;
            int backgroundsPerLine = (int) Math.ceil(maxStamina / 2);

            if (hasVigor && Energizer.CONFIG.vigor_wave) {

                vigorIndex = client.inGameHud.getTicks() % MathHelper.ceil(maxStamina + 5.0F);
            }

            if (currentStamina == maxStamina) {

                if (lastFullFillTime == -1) {

                    lastFullFillTime = client.inGameHud.getTicks();
                }
            }
            else {

                lastFullFillTime = -1;
            }

            int yDecrement = MethodeHelper.getYDecrement(maxStamina);

            for (int line = 0; line < lines; line++, fullIconsPerLine -= 10, backgroundsPerLine -= 10) {

                for (int i = 0; i < Math.min(backgroundsPerLine, 10); i++) {

                    int u = ((lastFullFillTime != -1) && ((client.inGameHud.getTicks() - lastFullFillTime) < 3) && Energizer.CONFIG.stamina_blink) ? 9 : 0;

                    int vigor = getVigorIndex(i, maxStamina, vigorIndex, line);

                    context.drawTexture(STAMINA_ICONS, x - i * 8, y - line * yDecrement - vigor, u, 0, 9, 9, 81, 9);
                }

                for (int i = 0; i < Math.min(fullIconsPerLine, 10); i++) {

                    int u = hasVigor ? 72 : stopSprint ? 36 : hasHunger ? 54 : 18;

                    int vigor = getVigorIndex(i, maxStamina, vigorIndex, line);

                    context.drawTexture(STAMINA_ICONS, x - i * 8, y - line * yDecrement - vigor, u, 0, 9, 9, 81, 9);
                }

                if (halfIcon && fullIconsPerLine < 10) {

                    int u = stopSprint ? 45 : hasHunger ? 63 : 27;
                    context.drawTexture(STAMINA_ICONS, x - fullIconsPerLine * 8, y - line * yDecrement, u, 0, 9, 9, 81, 9);
                    halfIcon = false;
                }
            }
        }
    }

    private int getVigorIndex(int i, float maxStamina, int vigorIndex, int line) {

        int index = 0;

        if ((i < maxStamina) && (i == (vigorIndex - (line * 10)))) {

            index = 2;
        }

        return index;
    }

    private static final Identifier HEART_ICONS = new Identifier("textures/gui/icons.png");

    @Inject(method = "renderHealthBar", at = @At("TAIL"))
    private void renderHealingHearts(DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {

        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null || client.player.getHealth() == client.player.getMaxHealth()) return;

        ItemStack itemStack = client.player.getMainHandStack();
        int healingAmount = MethodeHelper.getHealAmount(itemStack);

        if (healingAmount > 0) {

            int currentHealth = (int) client.player.getHealth();
            int totalHealth = Math.min(currentHealth + healingAmount, (int) client.player.getMaxHealth());
            int fullHearts = totalHealth / 2;
            boolean halfHeart = totalHealth % 2 == 1;

            boolean shouldBlink = (client.inGameHud.getTicks() % 30) < 15;
            float alpha = shouldBlink ? 0.5F : 0.25F;

            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);

            int yDecrement = MethodeHelper.getYDecrement(maxHealth);

            for (int i = currentHealth / 2; i < fullHearts; i++) {
                int row = i / 10;
                int col = i % 10;
                context.drawTexture(HEART_ICONS, x + col * 8, y - row * yDecrement, 52, 0, 9, 9);
            }

            if (halfHeart) {
                int row = fullHearts / 10;
                int col = fullHearts % 10;
                context.drawTexture(HEART_ICONS, x + col * 8, y - row * yDecrement, 61, 0, 9, 9);
            }

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();
        }
    }
}