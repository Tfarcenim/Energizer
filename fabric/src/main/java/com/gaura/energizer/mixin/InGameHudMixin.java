package com.gaura.energizer.mixin;

import com.gaura.energizer.Energizer;
import com.gaura.energizer.EnergizerFabric;
import com.gaura.energizer.utils.IPlayerEntity;
import com.gaura.energizer.utils.Utils;
import com.gaura.energizer.utils.MyHeartType;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public class InGameHudMixin {

    private static final int X_OFFSET = 82;
    private static final int Y_OFFSET = 39;

    private int lastFullFillTime = -1;

    @Inject(method = "renderPlayerHealth", at = @At("TAIL"))
    private void renderStaminaBar(GuiGraphics context, CallbackInfo ci) {

        Minecraft client = Minecraft.getInstance();

        if (client.player != null && ((InGameHudInvoker) this).invokeGetHeartCount(((InGameHudInvoker) this).invokeGetRiddenEntity()) == 0 && !(client.player.level().getDifficulty() == Difficulty.PEACEFUL && EnergizerFabric.CONFIG.disable_stamina_in_peaceful)) {

            int x = (client.getWindow().getGuiScaledWidth() / 2) + X_OFFSET + EnergizerFabric.CONFIG.x_offset_stamina_bar;
            int y = client.getWindow().getGuiScaledHeight() - Y_OFFSET - EnergizerFabric.CONFIG.y_offset_stamina_bar;

            int vigorIndex = -1;

            float maxStamina = (float) client.player.getAttributeValue(EnergizerFabric.STAMINA_ATTRIBUTE);
            float currentStamina = client.player.getEntityData().get(EnergizerFabric.STAMINA_DATA);

            boolean hasHunger = client.player.hasEffect(MobEffects.HUNGER);
            boolean hasVigor = client.player.hasEffect(EnergizerFabric.VIGOR);
            boolean stopSprint = ((IPlayerEntity) client.player).getStopSprint().getBoolean("stopSprint");

            int lines = (int) Math.ceil(maxStamina / 20);
            int fullIconsPerLine = (int) (currentStamina / 2);
            boolean halfIcon = Math.floor(currentStamina) % 2 != 0;
            int backgroundsPerLine = (int) Math.ceil(maxStamina / 2);

            if (hasVigor && EnergizerFabric.CONFIG.vigor_wave) {

                vigorIndex = client.gui.getGuiTicks() % Mth.ceil(maxStamina + 5.0F);
            }

            if (currentStamina == maxStamina) {

                if (lastFullFillTime == -1) {

                    lastFullFillTime = client.gui.getGuiTicks();
                }
            }
            else {

                lastFullFillTime = -1;
            }

            int yDecrement = Utils.getYDecrement(maxStamina);

            for (int line = 0; line < lines; line++, fullIconsPerLine -= 10, backgroundsPerLine -= 10) {

                for (int i = 0; i < Math.min(backgroundsPerLine, 10); i++) {

                    int u = ((lastFullFillTime != -1) && ((client.gui.getGuiTicks() - lastFullFillTime) < 3) && EnergizerFabric.CONFIG.stamina_blink) ? 9 : 0;

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

    private int getVigorIndex(int i, float maxStamina, int vigorIndex, int line) {

        int index = 0;

        if ((i < maxStamina) && (i == (vigorIndex - (line * 10)))) {

            index = 2;
        }

        return index;
    }

    private static final ResourceLocation ICONS = new ResourceLocation("textures/gui/icons.png");
    private final RandomSource random = RandomSource.create();

    @Inject(method = "renderHearts", at = @At("HEAD"), cancellable = true)
    private void renderHealthBar(GuiGraphics context, Player player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {

        if (!FabricLoader.getInstance().isModLoaded(EnergizerFabric.HEARTY_MEALS_MOD_ID) && EnergizerFabric.CONFIG.remove_hunger) {

            MyHeartType heartType = MyHeartType.fromPlayerState(player);
            int i = 9 * (player.level().getLevelData().isHardcore() ? 5 : 0);
            int j = Mth.ceil((double) maxHealth / 2.0);
            int k = Mth.ceil((double) absorption / 2.0);
            int l = j * 2;
            for (int m = j + k - 1; m >= 0; --m) {
                boolean bl3;
                int s;
                boolean bl;
                int n = m / 10;
                int o = m % 10;
                int p = x + o * 8;
                int q = y - n * lines;
                if (lastHealth + absorption <= 4) {
                    q += this.random.nextInt(2);
                }
                if (m < j && m == regeneratingHeartIndex) {
                    q -= 2;
                }
                this.myDrawHeart(context, MyHeartType.CONTAINER, p, q, i, blinking, false);
                int r = m * 2;
                boolean bl2 = bl = m >= j;
                if (bl && (s = r - l) < absorption) {
                    boolean bl22 = s + 1 == absorption;
                    this.myDrawHeart(context, heartType == MyHeartType.WITHERED ? heartType : MyHeartType.ABSORBING, p, q, i, false, bl22);
                }
                if (blinking && r < health) {
                    bl3 = r + 1 == health;
                    this.myDrawHeart(context, heartType, p, q, i, true, bl3);
                }

                // My code
                int healthAmount = Utils.getHealAmount(player.getMainHandItem());
                if (!(healthAmount > 0)) {
                    healthAmount = Utils.getHealAmount(player.getOffhandItem());
                }
                if (healthAmount > 0 && player.getHealth() < player.getMaxHealth() && r < lastHealth + healthAmount && r >= lastHealth - healthAmount) {
                    Minecraft client = Minecraft.getInstance();
                    int ticks = client.gui.getGuiTicks();
                    float opacity = (Mth.sin(ticks / EnergizerFabric.CONFIG.healing_animation_frequency) * 0.5f) + 0.5f;
                    bl3 = r + 1 == lastHealth + healthAmount;
                    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, opacity);
                    this.myDrawHeart(context, heartType, p, q, i, false, bl3);
                    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                }
                // End of my code

                if (r >= lastHealth) continue;
                bl3 = r + 1 == lastHealth;
                this.myDrawHeart(context, heartType, p, q, i, false, bl3);
            }

            ci.cancel();
        }
    }

    private void myDrawHeart(GuiGraphics context, MyHeartType type, int x, int y, int v, boolean blinking, boolean halfHeart) {

        context.blit(ICONS, x, y, type.getU(halfHeart, blinking), v, 9, 9);
    }
}