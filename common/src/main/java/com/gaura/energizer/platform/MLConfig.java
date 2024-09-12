package com.gaura.energizer.platform;

import com.gaura.energizer.config.HealFood;

public interface MLConfig {

    boolean removeHunger();
    boolean staminaBlink();
    boolean sprintKeybind();
    boolean lowerJump();
    boolean disableStaminaInPeaceful();
    boolean disableSprintSwimEmptyStamina();

    double staminaIncreaseHunger();

    double staminaIncreaseHungerEmpty();
    int staminaRegenerationDelay();
    double sprintingStaminaDecrease();
    double sprintingStaminaDecreaseHunger();
    double swimmingStaminaDecrease();
    double swimmingStaminaDecreaseHunger();
    boolean swimmingCostStamina();
    double staminaIncrease();
    double staminaIncreaseEmpty();

    HealFood[] healingFoodList();
    double healingAnimationFrequency();
    int xOffsetStaminaBar();
    int yOffsetStaminaBar();
    boolean vigorWave();

}
