package com.gaura.energizer;

import com.gaura.energizer.config.HealFood;
import com.gaura.energizer.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class Utils {

    public static int getYDecrement(float maxStamina) {

        int yDecrement;

        if (maxStamina <= 40) {
            yDecrement = 10;
        } else if (maxStamina <= 60) {
            yDecrement = 9;
        } else if (maxStamina <= 80) {
            yDecrement = 8;
        } else if (maxStamina <= 100) {
            yDecrement = 7;
        } else if (maxStamina <= 120) {
            yDecrement = 6;
        } else if (maxStamina <= 140) {
            yDecrement = 5;
        } else if (maxStamina <= 160) {
            yDecrement = 4;
        } else {
            yDecrement = 3;
        }

        return yDecrement;
    }

    public static int getHealAmount(ItemStack stack) {
        if (stack.isEmpty()) return 0;
        for (HealFood food : Services.PLATFORM.getConfig().healingFoodList()) {

            if (stack.getItem() == BuiltInRegistries.ITEM.get(new ResourceLocation(food.food()))) {

                return food.heal_amount();
            }
        }

        return 0;
    }
}
