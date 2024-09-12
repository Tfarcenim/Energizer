package com.gaura.energizer;

import java.util.Arrays;
import java.util.List;

public class Defaults {

    public static List<? extends String> convert() {
        return Arrays.stream(Energizer.default_heals).map(healFood -> healFood.food()+"|"+healFood.heal_amount()).toList();
    }

}
