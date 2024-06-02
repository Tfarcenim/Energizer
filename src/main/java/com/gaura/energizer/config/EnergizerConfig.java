package com.gaura.energizer.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "energizer")
@Config.Gui.Background("minecraft:textures/block/light_gray_wool.png")
public class EnergizerConfig implements ConfigData {

    @ConfigEntry.Gui.RequiresRestart
    @Comment("If the vigor potion should be available.")
    public boolean vigor_potion = true;

    @ConfigEntry.Gui.RequiresRestart
    @Comment("If the long vigor potion should be available.")
    public boolean vigor_potion_long = true;

    @Comment("If the vigor effect should restore stamina.")
    public boolean vigor_restore_stamina = true;

    @Comment("The x offset for the stamina bar.")
    public int x_offset = 82;

    @Comment("The y offset for the stamina bar.")
    public int y_offset = -39;

    @Comment("If the stamina bar should blink when stamina is full.")
    public boolean stamina_blink = true;

    @Comment("The multiplier for the amount of health restored by food.")
    public int heal_multiplier = 1;

    @Comment("The amount of stamina decreased per tick when sprinting.")
    public float stamina_decrease = 0.5F;

    @Comment("The amount of stamina decreased per tick when sprinting and hungry.")
    public float stamina_decrease_hunger = 1F;

    @Comment("The amount of stamina increased per tick when not sprinting.")
    public float stamina_increase = 0.5F;

    @Comment("The amount of stamina increased per tick when not sprinting and hungry.")
    public float stamina_increase_hunger = 0.25F;

    @Comment("The amount of stamina increased per tick when not sprinting and stamina completely empty.")
    public float stamina_increase_empty = 0.25F;

    @Comment("The amount of stamina increased per tick when not sprinting, hungry and stamina completely empty.")
    public float stamina_increase_hunger_empty = 0.125F;
}
