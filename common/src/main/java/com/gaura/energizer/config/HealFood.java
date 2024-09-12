package com.gaura.energizer.config;

import java.util.Objects;

public final class HealFood {
    private final String food;
    private final int heal_amount;

    public HealFood(String food, int heal_amount) {
        if (food == null) throw new NullPointerException();
        this.food = food;
        this.heal_amount = heal_amount;
    }

    public String food() {
        return food;
    }

    public int heal_amount() {
        return heal_amount;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (HealFood) obj;
        return Objects.equals(this.food, that.food) &&
                this.heal_amount == that.heal_amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(food, heal_amount);
    }

    @Override
    public String toString() {
        return "HealFood[" +
                "food=" + food + ", " +
                "heal_amount=" + heal_amount + ']';
    }


}
