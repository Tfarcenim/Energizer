package com.gaura.energizer.utils;

import com.gaura.energizer.Energizer;
import com.gaura.energizer.config.HealFood;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class MethodeHelper {

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

        for (HealFood food : Energizer.CONFIG.healFoods) {

            if (stack.getItem() == Registries.ITEM.get(new Identifier(food.food))) {

                return food.heal_amount;
            }
        }

        return 0;
    }
}
