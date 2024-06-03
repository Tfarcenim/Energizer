package com.gaura.energizer.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "energizer")
@Config.Gui.Background("minecraft:textures/block/light_gray_wool.png")
public class EnergizerConfig implements ConfigData {

    // STAMINA

    @ConfigEntry.Category("stamina")
    @Comment("The amount of stamina decreased per tick when sprinting.")
    public float stamina_decrease = 0.5F;

    @ConfigEntry.Category("stamina")
    @Comment("The amount of stamina decreased per tick when sprinting and hungry.")
    public float stamina_decrease_hunger = 1F;

    @ConfigEntry.Category("stamina")
    @Comment("The amount of stamina increased per tick when not sprinting.")
    public float stamina_increase = 0.5F;

    @ConfigEntry.Category("stamina")
    @Comment("The amount of stamina increased per tick when not sprinting and hungry.")
    public float stamina_increase_hunger = 0.25F;

    @ConfigEntry.Category("stamina")
    @Comment("The amount of stamina increased per tick when not sprinting and stamina completely empty.")
    public float stamina_increase_empty = 0.25F;

    @ConfigEntry.Category("stamina")
    @Comment("The amount of stamina increased per tick when not sprinting, hungry and stamina completely empty.")
    public float stamina_increase_hunger_empty = 0.125F;

    @ConfigEntry.Category("stamina")
    @Comment("If the stamina bar should blink when stamina is full.")
    public boolean stamina_blink = true;

    @ConfigEntry.Category("stamina")
    @Comment("If the stamina bar should wave when having Vigor effect.")
    public boolean vigor_wave = true;

    @ConfigEntry.Category("stamina")
    @Comment("The x offset for the stamina bar.")
    public int x_offset = 82;

    @ConfigEntry.Category("stamina")
    @Comment("The y offset for the stamina bar.")
    public int y_offset = -39;

    // POTION

    @ConfigEntry.Category("potion")
    @ConfigEntry.Gui.RequiresRestart
    @Comment("If the vigor potion should be available.")
    public boolean vigor_potion = true;

    @ConfigEntry.Category("potion")
    @ConfigEntry.Gui.RequiresRestart
    @Comment("The time the vigor potion should last (in seconds).")
    public int vigor_potion_time = 10;

    @ConfigEntry.Category("potion")
    @ConfigEntry.Gui.RequiresRestart
    @Comment("If the long vigor potion should be available.")
    public boolean vigor_potion_long = true;

    @ConfigEntry.Category("potion")
    @ConfigEntry.Gui.RequiresRestart
    @Comment("The time the long vigor potion should last (in seconds).")
    public int vigor_potion_long_time = 30;

    // HEAL

    @ConfigEntry.Category("heal")
    @Comment("The multiplier for the amount of health restored by food.")
    public int heal_multiplier = 1;
}
