package com.gaura.energizer.config;

public record HealFood(String food, int heal_amount) {

    public HealFood() {
        this(null,0);
    }
}
