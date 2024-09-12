package com.gaura.energizer.config;

import com.gaura.energizer.Defaults;
import com.gaura.energizer.platform.MLConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "energizer")
@Config.Gui.Background("minecraft:textures/block/yellow_wool.png")
public class EnergizerConfig implements ConfigData, MLConfig {

    // STAMINA

    @ConfigEntry.Category("stamina")
    @Comment("If you will not be able to sprint or swim when you run out of stamina and wait for it to completely regenerate.")
    public boolean disable_sprint_swim_empty_stamina = true;

    @ConfigEntry.Category("stamina")
    @Comment("The amount of stamina decreased per tick when sprinting.")
    public float sprinting_stamina_decrease = 0.5F;

    @ConfigEntry.Category("stamina")
    @Comment("The amount of stamina decreased per tick when sprinting and hungry.")
    public float sprinting_stamina_decrease_hunger = 1F;

    @ConfigEntry.Category("stamina")
    @Comment("If swimming should cost stamina.")
    public boolean swimming_cost_stamina = true;

    @ConfigEntry.Category("stamina")
    @Comment("The amount of stamina decreased per tick when swimming.")
    public float swimming_stamina_decrease = 0.5F;

    @ConfigEntry.Category("stamina")
    @Comment("The amount of stamina decreased per tick when swimming and hungry.")
    public float swimming_stamina_decrease_hunger = 1F;

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
    @Comment("The delay in ticks before the stamina starts regenerating.")
    public int stamina_regeneration_delay = 10;

    @ConfigEntry.Category("stamina")
    @Comment("If the stamina bar should blink when stamina is full.")
    public boolean stamina_blink = true;

    @ConfigEntry.Category("stamina")
    @Comment("If the stamina bar should wave when having Vigor effect.")
    public boolean vigor_wave = true;

    @ConfigEntry.Category("stamina")
    @Comment("If you must keep pressing the sprint keybind to sprint, rather than toggling it.")
    public boolean sprint_keybind = true;

    @ConfigEntry.Category("stamina")
    @Comment("If the player will jump lower when the stamina bar is empty.")
    public boolean lower_jump = true;

    @ConfigEntry.Category("stamina")
    @Comment("The multiplier for the jump height when the stamina bar is empty.")
    public float lower_jump_multiplier = 0.75F;

    @ConfigEntry.Category("stamina")
    @Comment("If the player will walk slower when the stamina bar is empty.")
    public boolean slower_walk = true;

    @ConfigEntry.Category("stamina")
    @Comment("The multiplier for the walk speed when the stamina bar is empty.")
    public float slower_walk_multiplier = 0.5F;

    @ConfigEntry.Category("stamina")
    @Comment("If Stamina should be disabled in Peaceful.")
    public boolean disable_stamina_in_peaceful = false;

    @ConfigEntry.Category("stamina")
    @Comment("The x offset for the stamina bar.")
    public int x_offset_stamina_bar = 0;

    @ConfigEntry.Category("stamina")
    @Comment("The y offset for the stamina bar.")
    public int y_offset_stamina_bar = 0;

    // HUNGER

    @ConfigEntry.Category("hunger")
    @Comment("If the hunger should be removed.")
    public boolean remove_hunger = true;

    @ConfigEntry.Category("hunger")
    @Comment("If the player can sprint when the hunger bar is below 6 (3 icons).")
    public boolean can_continue_sprinting = false;

    @ConfigEntry.Category("hunger")
    @Comment("The x offset for the hunger bar.")
    public int x_offset_hunger_bar = 0;

    @ConfigEntry.Category("hunger")
    @Comment("The y offset for the hunger bar.")
    public int y_offset_hunger_bar = 0;

    // AIR

    @ConfigEntry.Category("air")
    @Comment("If the air bar should be synced with the stamina bar (on top of it).")
    public boolean sync_with_stamina_bar = true;

    @ConfigEntry.Category("air")
    @Comment("The x offset for the air bar.")
    public int x_offset_air_bar = 0;

    @ConfigEntry.Category("air")
    @Comment("The y offset for the air bar.")
    public int y_offset_air_bar = 0;

    // HEAL

    @ConfigEntry.Category("heal")
    @Comment("The frequency for the healing animation.")
    public float healing_animation_frequency = 5.0f;

    @ConfigEntry.Category("heal")
    public HealFood[] healing_food_list = Defaults.default_heals;

    @Override
    public boolean removeHunger() {
        return remove_hunger;
    }

    @Override
    public boolean staminaBlink() {
        return stamina_blink;
    }

    @Override
    public boolean sprintKeybind() {
        return sprint_keybind;
    }

    @Override
    public boolean lowerJump() {
        return lower_jump;
    }

    @Override
    public boolean disableSprintSwimEmptyStamina() {
        return disable_sprint_swim_empty_stamina;
    }

    @Override
    public double staminaIncrease() {
        return stamina_increase;
    }

    @Override
    public double staminaIncreaseEmpty() {
        return stamina_increase_empty;
    }

    @Override
    public double staminaIncreaseHunger() {
        return stamina_increase_hunger;
    }

    @Override
    public double staminaIncreaseHungerEmpty() {
        return stamina_increase_hunger_empty;
    }

    @Override
    public boolean disableStaminaInPeaceful() {
        return disable_stamina_in_peaceful;
    }

    @Override
    public int staminaRegenerationDelay() {
        return stamina_regeneration_delay;
    }

    @Override
    public double sprintingStaminaDecrease() {
        return sprinting_stamina_decrease;
    }

    @Override
    public double sprintingStaminaDecreaseHunger() {
        return sprinting_stamina_decrease_hunger;
    }

    @Override
    public double swimmingStaminaDecrease() {
        return swimming_stamina_decrease;
    }

    @Override
    public double swimmingStaminaDecreaseHunger() {
        return swimming_stamina_decrease_hunger;
    }

    @Override
    public boolean swimmingCostStamina() {
        return swimming_cost_stamina;
    }

    @Override
    public HealFood[] healingFoodList() {
        return healing_food_list;
    }

    @Override
    public double healingAnimationFrequency() {
        return healing_animation_frequency;
    }

    @Override
    public int xOffsetStaminaBar() {
        return x_offset_stamina_bar;
    }

    @Override
    public int yOffsetStaminaBar() {
        return y_offset_stamina_bar;
    }

    @Override
    public boolean vigorWave() {
        return vigor_wave;
    }

    @Override
    public boolean canContinueSprinting() {
        return can_continue_sprinting;
    }

    @Override
    public double lowerJumpMultiplier() {
        return lower_jump_multiplier;
    }

    @Override
    public double slowerWalkMultiplier() {
        return slower_walk_multiplier;
    }

    @Override
    public boolean slowerWalk() {
        return slower_walk;
    }
}
