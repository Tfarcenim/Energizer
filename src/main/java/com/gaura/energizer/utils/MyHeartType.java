package com.gaura.energizer.utils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

@Environment(value= EnvType.CLIENT)
public enum MyHeartType {

    CONTAINER(0, false),
    NORMAL(2, true),
    POISONED(4, true),
    WITHERED(6, true),
    ABSORBING(8, false),
    FROZEN(9, false);

    private final int textureIndex;
    private final boolean hasBlinkingTexture;

    MyHeartType(int textureIndex, boolean hasBlinkingTexture) {

        this.textureIndex = textureIndex;
        this.hasBlinkingTexture = hasBlinkingTexture;
    }

    public int getU(boolean halfHeart, boolean blinking) {

        int i;

        if (this == CONTAINER) {

            i = blinking ? 1 : 0;
        }
        else {

            int j = halfHeart ? 1 : 0;
            int k = this.hasBlinkingTexture && blinking ? 2 : 0;
            i = j + k;
        }

        return 16 + (this.textureIndex * 2 + i) * 9;
    }

    public static MyHeartType fromPlayerState(PlayerEntity player) {

        return player.hasStatusEffect(StatusEffects.POISON) ? POISONED : (player.hasStatusEffect(StatusEffects.WITHER) ? WITHERED : (player.isFrozen() ? FROZEN : NORMAL));
    }
}
