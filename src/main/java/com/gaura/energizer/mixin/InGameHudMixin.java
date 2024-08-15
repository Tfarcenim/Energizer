package com.gaura.energizer.mixin;

import com.gaura.energizer.Energizer;
import com.gaura.energizer.utils.IPlayerEntity;
import com.gaura.energizer.utils.Utils;
import com.gaura.energizer.utils.MyHeartType;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameMode;
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

        if (client.player != null && ((InGameHudInvoker) this).invokeGetHeartCount(((InGameHudInvoker) this).invokeGetRiddenEntity()) == 0 && !(client.player.getWorld().getDifficulty() == Difficulty.PEACEFUL && Energizer.CONFIG.disable_stamina_in_peaceful)) {

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

            int yDecrement = Utils.getYDecrement(maxStamina);

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

    private static final Identifier ICONS = new Identifier("textures/gui/icons.png");
    private final Random random = Random.create();

    @Inject(method = "renderHealthBar", at = @At("HEAD"), cancellable = true)
    private void renderHealthBar(DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {

        if (!FabricLoader.getInstance().isModLoaded(Energizer.HEARTY_MEALS_MOD_ID)) {

            MyHeartType heartType = MyHeartType.fromPlayerState(player);
            int i = 9 * (player.getWorld().getLevelProperties().isHardcore() ? 5 : 0);
            int j = MathHelper.ceil((double) maxHealth / 2.0);
            int k = MathHelper.ceil((double) absorption / 2.0);
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
                int healthAmount = Utils.getHealAmount(player.getMainHandStack());
                if (!(healthAmount > 0)) {
                    healthAmount = Utils.getHealAmount(player.getOffHandStack());
                }
                if (healthAmount > 0 && player.getHealth() < player.getMaxHealth() && r < lastHealth + healthAmount && r >= lastHealth - healthAmount) {
                    MinecraftClient client = MinecraftClient.getInstance();
                    int ticks = client.inGameHud.getTicks();
                    float opacity = (MathHelper.sin(ticks / 5.0f) * 0.5f) + 0.5f;
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

    private void myDrawHeart(DrawContext context, MyHeartType type, int x, int y, int v, boolean blinking, boolean halfHeart) {

        context.drawTexture(ICONS, x, y, type.getU(halfHeart, blinking), v, 9, 9);
    }
}