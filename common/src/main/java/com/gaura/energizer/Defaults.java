package com.gaura.energizer;

import com.gaura.energizer.config.HealFood;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Defaults {

    public static final HealFood[] healfoods = {

            new HealFood("minecraft:apple", 2),
            new HealFood("minecraft:baked_potato", 3),
            new HealFood("minecraft:beef", 1),
            new HealFood("minecraft:beetroot", 1),
            new HealFood("minecraft:beetroot_soup", 5),
            new HealFood("minecraft:bread", 3),
            new HealFood("minecraft:cake", 1),
            new HealFood("minecraft:carrot", 2),
            new HealFood("minecraft:chicken", 1),
            new HealFood("minecraft:chorus_fruit", 1),
            new HealFood("minecraft:cod", 1),
            new HealFood("minecraft:cooked_beef", 4),
            new HealFood("minecraft:cooked_chicken", 3),
            new HealFood("minecraft:cooked_cod", 3),
            new HealFood("minecraft:cooked_mutton", 3),
            new HealFood("minecraft:cooked_porkchop", 4),
            new HealFood("minecraft:cooked_rabbit", 3),
            new HealFood("minecraft:cooked_salmon", 3),
            new HealFood("minecraft:cookie", 1),
            new HealFood("minecraft:dried_kelp", 1),
            new HealFood("minecraft:enchanted_golden_apple", 4),
            new HealFood("minecraft:golden_apple", 3),
            new HealFood("minecraft:golden_carrot", 4),
            new HealFood("minecraft:honey_bottle", 2),
            new HealFood("minecraft:melon_slice", 1),
            new HealFood("minecraft:mushroom_stew", 5),
            new HealFood("minecraft:mutton", 1),
            new HealFood("minecraft:poisonous_potato", 1),
            new HealFood("minecraft:porkchop", 1),
            new HealFood("minecraft:potato", 1),
            new HealFood("minecraft:pufferfish", 1),
            new HealFood("minecraft:pumpkin_pie", 5),
            new HealFood("minecraft:rabbit", 1),
            new HealFood("minecraft:rabbit_stew", 5),
            new HealFood("minecraft:rotten_flesh", 1),
            new HealFood("minecraft:salmon", 1),
            new HealFood("minecraft:spider_eye", 1),
            new HealFood("minecraft:suspicious_stew", 5),
            new HealFood("minecraft:sweet_berries", 1),
            new HealFood("minecraft:glow_berries", 1),
            new HealFood("minecraft:tropical_fish", 1)
    };

    public static List<? extends String> convert() {
        return Arrays.stream(healfoods).map(healFood -> healFood.food()+"|"+healFood.heal_amount()).toList();
    }

}
