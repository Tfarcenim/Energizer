package com.gaura.energizer.config;

public class HealFood {

    public String food;

    public int heal_amount = 0;

    public HealFood() {}

    public HealFood(String food, int heal_amount) {

        this.food = food;
        this.heal_amount = heal_amount;
    }
}
