package com.gaura.energizer;

import com.gaura.energizer.init.ModObjects;
import com.gaura.energizer.mixin.InGameHudInvoker;
import com.gaura.energizer.platform.MLConfig;
import com.gaura.energizer.platform.Services;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class Huds {

    public static void renderHealed(GuiGraphics context, Player player, int x, int y, int lines, int regeneratingHeartIndex,
                                    float maxHealth, int lastHealth, int health, int absorption, boolean blinking,
                                   Gui.HeartType heartType,int i,int j, int k,int l,int m, int n,int o, int p, int q,int r) {
        // My code
        int healthAmount = Utils.getHealAmount(player.getMainHandItem());
        if (!(healthAmount > 0)) {
            healthAmount = Utils.getHealAmount(player.getOffhandItem());
        }
        if (healthAmount > 0 && player.getHealth() < player.getMaxHealth() && r < lastHealth + healthAmount && r >= lastHealth - healthAmount) {
            Minecraft client = Minecraft.getInstance();
            int ticks = client.gui.getGuiTicks();
            float opacity = (Mth.sin((float) (ticks / Services.PLATFORM.getConfig().healingAnimationFrequency())) * 0.5f) + 0.5f;
            boolean bl3 = r + 1 == lastHealth + healthAmount;
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, opacity);
            Minecraft.getInstance().gui.renderHeart(context, heartType, p, q, i, false, bl3);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
        // End of my code
    }

    static int lastFullFillTime;

    public static void renderStamina(Gui gui,GuiGraphics context,int screenWidth,int screenHeight) {

        Minecraft client = Minecraft.getInstance();
        MLConfig config = Services.PLATFORM.getConfig();

        if (client.player != null && ((InGameHudInvoker) gui).invokeGetHeartCount(((InGameHudInvoker) gui).invokeGetRiddenEntity()) == 0 && !(client.player.level().getDifficulty() == Difficulty.PEACEFUL && config.disableStaminaInPeaceful())) {

            int x = (screenWidth / 2) + Services.PLATFORM.getBaseXOffset() + config.xOffsetStaminaBar();
            int y = screenHeight - Services.PLATFORM.getBaseYOffset() - config.yOffsetStaminaBar();

            int vigorIndex = -1;
            IPlayerEntity iPlayerEntity = (IPlayerEntity) client.player;
            float maxStamina = iPlayerEntity.getMaxStamina();

            float currentStamina = iPlayerEntity.getStamina();

            boolean hasHunger = client.player.hasEffect(MobEffects.HUNGER);
            boolean hasVigor = client.player.hasEffect(ModObjects.VIGOR);
            boolean stopSprint = iPlayerEntity.getStopSprint();

            int lines = (int) Math.ceil(maxStamina / 20);
            int fullIconsPerLine = (int) (currentStamina / 2);
            boolean halfIcon = Math.floor(currentStamina) % 2 != 0;
            int backgroundsPerLine = (int) Math.ceil(maxStamina / 2);

            if (hasVigor && config.vigorWave()) {

                vigorIndex = client.gui.getGuiTicks() % Mth.ceil(maxStamina + 5.0F);
            }

            if (currentStamina == maxStamina) {

                if (lastFullFillTime == -1) {

                    lastFullFillTime = client.gui.getGuiTicks();
                }
            } else {

                lastFullFillTime = -1;
            }

            int yDecrement = Utils.getYDecrement(maxStamina);

            for (int line = 0; line < lines; line++, fullIconsPerLine -= 10, backgroundsPerLine -= 10) {

                for (int i = 0; i < Math.min(backgroundsPerLine, 10); i++) {

                    int u = ((lastFullFillTime != -1) && ((client.gui.getGuiTicks() - lastFullFillTime) < 3) && config.staminaBlink()) ? 9 : 0;

                    int vigor = getVigorIndex(i, maxStamina, vigorIndex, line);

                    context.blit(Energizer.STAMINA_ICONS, x - i * 8, y - line * yDecrement - vigor, u, 0, 9, 9, 81, 9);
                }

                for (int i = 0; i < Math.min(fullIconsPerLine, 10); i++) {

                    int u = hasVigor ? 72 : stopSprint ? 36 : hasHunger ? 54 : 18;

                    int vigor = getVigorIndex(i, maxStamina, vigorIndex, line);

                    context.blit(Energizer.STAMINA_ICONS, x - i * 8, y - line * yDecrement - vigor, u, 0, 9, 9, 81, 9);
                }

                if (halfIcon && fullIconsPerLine < 10) {

                    int u = stopSprint ? 45 : hasHunger ? 63 : 27;
                    context.blit(Energizer.STAMINA_ICONS, x - fullIconsPerLine * 8, y - line * yDecrement, u, 0, 9, 9, 81, 9);
                    halfIcon = false;
                }
            }
        }
    }

    private static int getVigorIndex(int i, float maxStamina, int vigorIndex, int line) {

        int index = 0;

        if ((i < maxStamina) && (i == (vigorIndex - (line * 10)))) {

            index = 2;
        }

        return index;
    }


}
