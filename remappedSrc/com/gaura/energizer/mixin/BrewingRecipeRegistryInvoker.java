package com.gaura.energizer.mixin;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PotionBrewing.class)
public interface BrewingRecipeRegistryInvoker {

    @Invoker("registerPotionRecipe")
    static void invokeRegisterPotionRecipe(Potion input, Item item, Potion output) {

        throw new AssertionError();
    }
}
