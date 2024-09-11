package com.gaura.energizer;

import com.gaura.energizer.config.EnergizerConfig;
import com.gaura.energizer.effect.VigorEffect;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;

public class EnergizerFabric implements ModInitializer {

	public static final String HEARTY_MEALS_MOD_ID = "heartymeals";

	public static EnergizerConfig CONFIG = new EnergizerConfig();

	// Stamina attribute
	public static final Attribute STAMINA_ATTRIBUTE = new RangedAttribute("attribute.name." + Energizer.MOD_ID + ".player.stamina", 20, 1, 1024).setSyncable(true);

	// Stamina data
	public static final EntityDataAccessor<Float> STAMINA_DATA = SynchedEntityData.defineId(Player.class, EntityDataSerializers.FLOAT);

	// Vigor effect
	public static final VigorEffect VIGOR = new VigorEffect(MobEffectCategory.BENEFICIAL, 0x14B2FF);

	// Vigor potions
	public static final Potion VIGOR_POTION = new Potion(new MobEffectInstance(VIGOR, CONFIG.vigor_potion_time * 20, 0));
	public static final Potion VIGOR_POTION_LONG = new Potion(new MobEffectInstance(VIGOR, CONFIG.vigor_potion_long_time * 20, 0));

	@Override
	public void onInitialize() {

		// Registering Config
		AutoConfig.register(EnergizerConfig.class, JanksonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(EnergizerConfig.class).getConfig();

		// Registering Stamina attribute
		Registry.register(BuiltInRegistries.ATTRIBUTE, new ResourceLocation(Energizer.MOD_ID, "generic.stamina"), STAMINA_ATTRIBUTE);

		// Registering Vigor effect
		Registry.register(BuiltInRegistries.MOB_EFFECT, new ResourceLocation(Energizer.MOD_ID, "vigor"), VIGOR);

		if (CONFIG.vigor_potion) {

			// Registering Vigor potion
			Registry.register(BuiltInRegistries.POTION, new ResourceLocation(Energizer.MOD_ID, "vigor_potion"), VIGOR_POTION);
			PotionBrewing.addMix(Potions.AWKWARD, Items.BEETROOT, VIGOR_POTION);
		}

		if (CONFIG.vigor_potion_long) {

			// Registering Vigor long potion
			Registry.register(BuiltInRegistries.POTION, new ResourceLocation(Energizer.MOD_ID, "vigor_potion_long"), VIGOR_POTION_LONG);
			PotionBrewing.addMix(VIGOR_POTION, Items.REDSTONE, VIGOR_POTION_LONG);
		}
	}
}