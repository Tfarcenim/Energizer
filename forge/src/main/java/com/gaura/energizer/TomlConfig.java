package com.gaura.energizer;

import com.gaura.energizer.config.HealFood;
import com.gaura.energizer.platform.MLConfig;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class TomlConfig implements MLConfig {

    public static final Server SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;

    static {
        final Pair<Server, ForgeConfigSpec> specPair2 = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER_SPEC = specPair2.getRight();
        SERVER = specPair2.getLeft();
    }

    public static class Server {
        public static ForgeConfigSpec.BooleanValue removeHunger;
        public static ForgeConfigSpec.BooleanValue canContinueSprinting;

        public static ForgeConfigSpec.BooleanValue staminaBlink;
        public static ForgeConfigSpec.BooleanValue vigorWave;
        public static ForgeConfigSpec.BooleanValue sprintKeybind;
        public static ForgeConfigSpec.BooleanValue lowerJump;
        public static ForgeConfigSpec.BooleanValue disableSprintSwimEmptyStamina;
        public static ForgeConfigSpec.BooleanValue disableStaminaInPeaceful;
        public static ForgeConfigSpec.DoubleValue staminaIncrease;
        public static ForgeConfigSpec.DoubleValue staminaIncreaseEmpty;
        public static ForgeConfigSpec.DoubleValue staminaIncreaseHunger;
        public static ForgeConfigSpec.DoubleValue staminaIncreaseHungerEmpty;
        public static ForgeConfigSpec.IntValue staminaRegenerationDelay;
        public static ForgeConfigSpec.DoubleValue sprintingStaminaDecrease;
        public static ForgeConfigSpec.DoubleValue sprintingStaminaDecreaseHunger;

        public static ForgeConfigSpec.DoubleValue swimmingStaminaDecrease;
        public static ForgeConfigSpec.DoubleValue swimmingStaminaDecreaseHunger;
        public static ForgeConfigSpec.BooleanValue swimmingCostStamina;
        public static ForgeConfigSpec.ConfigValue<List<? extends String>> healingFoodList;
        public static ForgeConfigSpec.IntValue xOffsetStaminaBar;
        public static ForgeConfigSpec.IntValue yOffsetStaminaBar;

        public static ForgeConfigSpec.DoubleValue lowerJumpMultiplier;

        public static ForgeConfigSpec.BooleanValue slowerWalk;
        public static ForgeConfigSpec.DoubleValue slowerWalkMultiplier;

        public static ForgeConfigSpec.DoubleValue healingAnimationFrequency;

        public Server(ForgeConfigSpec.Builder builder) {
            builder.push("hunger");
            removeHunger = builder.comment("If the hunger should be removed.").define("remove_hunger",true);
            canContinueSprinting = builder.comment("If the player can sprint when the hunger bar is below 6 (3 icons).").define("can_continue_sprinting",false);

            builder.pop();
            builder.push("stamina");
            staminaBlink = builder.comment("If the stamina bar should blink when stamina is full.").define("stamina_blink",true);
            vigorWave = builder.comment("If the stamina bar should wave when having Vigor effect.").define("vigor_wave",true);
            sprintKeybind = builder.comment("If you must keep pressing the sprint keybind to sprint, rather than toggling it.").define("sprint_keybind",true);
            lowerJump = builder.comment("If the player will jump lower when the stamina bar is empty.").define("lower_jump",true);
            disableSprintSwimEmptyStamina = builder.comment("If you will not be able to sprint or swim when you run out of stamina and wait for it to completely regenerate.")
                    .define("disable_sprint_swim_empty_stamina",true);

            staminaIncrease = builder.comment("The amount of stamina increased per tick when not sprinting.")
                    .defineInRange("stamina_increase",.5,0,Float.MAX_VALUE);

            staminaIncreaseEmpty = builder.comment("The amount of stamina increased per tick when not sprinting and stamina completely empty.")
                    .defineInRange("stamina_increase_empty",.25,0,Float.MAX_VALUE);
            staminaIncreaseHunger = builder.comment("The amount of stamina increased per tick when not sprinting and hungry.")
                    .defineInRange("stamina_increase_hunger",.25,0,Float.MAX_VALUE);
            staminaIncreaseHungerEmpty = builder.comment("The amount of stamina increased per tick when not sprinting, hungry and stamina completely empty.")
                    .defineInRange("stamina_increase_hunger_empty",.125,0,Float.MAX_VALUE);

            disableStaminaInPeaceful = builder.comment("If Stamina should be disabled in Peaceful.")
                    .define("disable_stamina_in_peaceful",false);

            staminaRegenerationDelay = builder.comment("The delay in ticks before the stamina starts regenerating.")
                    .defineInRange("stamina_regeneration_delay",10,0,Integer.MAX_VALUE);

            staminaIncreaseHungerEmpty = builder.comment("The amount of stamina increased per tick when not sprinting, hungry and stamina completely empty.")
                    .defineInRange("stamina_increase_hunger_empty",.125,0,Float.MAX_VALUE);

            sprintingStaminaDecrease = builder.comment("The amount of stamina decreased per tick when sprinting.")
                    .defineInRange("sprinting_stamina_decrease",.5,0,Float.MAX_VALUE);

            sprintingStaminaDecreaseHunger = builder.comment("The amount of stamina decreased per tick when sprinting and hungry.")
                    .defineInRange("sprinting_stamina_decrease_hunger",1,0,Float.MAX_VALUE);

            swimmingStaminaDecrease = builder.comment("The amount of stamina decreased per tick when swimming.")
                    .defineInRange("swimming_stamina_decrease",.5,0,Float.MAX_VALUE);

            swimmingStaminaDecreaseHunger = builder.comment("The amount of stamina decreased per tick when swimming and hungry.")
                    .defineInRange("swimming_stamina_decrease_hunger",1,0,Float.MAX_VALUE);

            swimmingCostStamina = builder.comment("If swimming should cost stamina.")
                            .define("swimming_cost_stamina",true);

            lowerJumpMultiplier = builder.comment("The multiplier for the jump height when the stamina bar is empty.")
                    .defineInRange("lower_jump_multiplier",.75,0,1);

            slowerWalk = builder.comment("If the player will walk slower when the stamina bar is empty.")
                    .define("slower_walk",true);

            slowerWalkMultiplier = builder.comment("The multiplier for the walk speed when the stamina bar is empty.")
                    .defineInRange("slower_walk_multiplier",.5,0,1);

            //clientside
            xOffsetStaminaBar = builder.comment("The x offset for the stamina bar.").defineInRange("x_offset_stamina_bar",0,-10000,10000);
            yOffsetStaminaBar = builder.comment("The y offset for the stamina bar.").defineInRange("y_offset_stamina_bar",0,-10000,10000);


            builder.pop();
            builder.push("heal");
            //clientside
            healingAnimationFrequency = builder.comment("The frequency for the healing animation.")
                    .defineInRange("healing_animation_frequency",5,0,Float.MAX_VALUE);
            healingFoodList = builder.defineList("healing_food_list",Defaults.convert(),o -> o instanceof String);
            builder.pop();
        }
    }

    static HealFood[] cache;

    public static void configEvent(ModConfigEvent event) {
        if (event.getConfig().getModId().equals(Energizer.MOD_ID)) {
            cache = Server.healingFoodList.get().stream().map(s -> {
                String[] strings = s.split("\\|");
                HealFood healFood = new HealFood(strings[0],Integer.parseInt(strings[1]));
                return healFood;
            }).toArray(HealFood[]::new);
        }
    }

    @Override
    public boolean removeHunger() {
        return Server.removeHunger.get();
    }

    @Override
    public boolean staminaBlink() {
        return Server.staminaBlink.get();
    }

    @Override
    public boolean sprintKeybind() {
        return Server.sprintKeybind.get();
    }

    @Override
    public boolean lowerJump() {
        return Server.lowerJump.get();
    }

    @Override
    public boolean disableSprintSwimEmptyStamina() {
        return Server.disableSprintSwimEmptyStamina.get();
    }

    @Override
    public double staminaIncreaseEmpty() {
        return Server.staminaIncreaseEmpty.get();
    }

    @Override
    public double staminaIncreaseHunger() {
        return Server.staminaIncreaseHunger.get();
    }

    @Override
    public double staminaIncreaseHungerEmpty() {
        return Server.staminaIncreaseHungerEmpty.get();
    }

    @Override
    public boolean disableStaminaInPeaceful() {
        return Server.disableStaminaInPeaceful.get();
    }

    @Override
    public int staminaRegenerationDelay() {
        return Server.staminaRegenerationDelay.get();
    }

    @Override
    public double sprintingStaminaDecrease() {
        return Server.sprintingStaminaDecrease.get();
    }

    @Override
    public double sprintingStaminaDecreaseHunger() {
        return Server.sprintingStaminaDecreaseHunger.get();
    }

    @Override
    public double swimmingStaminaDecrease() {
        return Server.swimmingStaminaDecrease.get();
    }

    @Override
    public double swimmingStaminaDecreaseHunger() {
        return Server.swimmingStaminaDecreaseHunger.get();
    }

    @Override
    public boolean swimmingCostStamina() {
        return Server.swimmingCostStamina.get();
    }

    @Override
    public double staminaIncrease() {
        return Server.staminaIncrease.get();
    }

    @Override
    public HealFood[] healingFoodList() {
        return cache;
    }

    @Override
    public double healingAnimationFrequency() {
        return Server.healingAnimationFrequency.get();
    }

    @Override
    public int xOffsetStaminaBar() {
        return Server.xOffsetStaminaBar.get();
    }

    @Override
    public int yOffsetStaminaBar() {
        return Server.yOffsetStaminaBar.get();
    }

    @Override
    public boolean vigorWave() {
        return Server.vigorWave.get();
    }

    @Override
    public boolean canContinueSprinting() {
        return Server.canContinueSprinting.get();
    }

    @Override
    public double lowerJumpMultiplier() {
        return Server.lowerJumpMultiplier.get();
    }

    @Override
    public double slowerWalkMultiplier() {
        return Server.slowerWalkMultiplier.get();
    }

    @Override
    public boolean slowerWalk() {
        return Server.slowerWalk.get();
    }
}
