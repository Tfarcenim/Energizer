package com.gaura.energizer;

import com.gaura.energizer.config.EnergizerConfig;
import com.gaura.energizer.init.ModObjects;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;

public class EnergizerFabric implements ModInitializer {

	public static EnergizerConfig CONFIG = new EnergizerConfig();

    // Vigor potions
	public static final Potion VIGOR_POTION = new Potion(new MobEffectInstance(ModObjects.VIGOR, CONFIG.vigor_potion_time * 20, 0));
	public static final Potion VIGOR_POTION_LONG = new Potion(new MobEffectInstance(ModObjects.VIGOR, CONFIG.vigor_potion_long_time * 20, 0));

	@Override
	public void onInitialize() {

		// Registering Config
		AutoConfig.register(EnergizerConfig.class, JanksonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(EnergizerConfig.class).getConfig();

		// Registering Stamina attribute
		Registry.register(BuiltInRegistries.ATTRIBUTE, Energizer.id("generic.stamina"), ModObjects.STAMINA_ATTRIBUTE);

		// Registering Vigor effect
		Registry.register(BuiltInRegistries.MOB_EFFECT, Energizer.id("vigor"), ModObjects.VIGOR);

        // Registering Vigor potion
        Registry.register(BuiltInRegistries.POTION, Energizer.id("vigor_potion"), VIGOR_POTION);
        PotionBrewing.addMix(Potions.AWKWARD, Items.BEETROOT, VIGOR_POTION);

        // Registering Vigor long potion
        Registry.register(BuiltInRegistries.POTION, new ResourceLocation(Energizer.MOD_ID, "vigor_potion_long"), VIGOR_POTION_LONG);
        PotionBrewing.addMix(VIGOR_POTION, Items.REDSTONE, VIGOR_POTION_LONG);
        Energizer.init();
	}
}