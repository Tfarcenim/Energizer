package com.gaura.energizer;

import com.gaura.energizer.init.ModObjects;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;

@Mod(Energizer.MOD_ID)
public class EnergizerForge {
    
    public EnergizerForge() {
    
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
    
        // Use Forge to bootstrap the Common mod.
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER,TomlConfig.SERVER_SPEC);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(TomlConfig::configEvent);
        bus.addListener(this::register);
        bus.addListener(this::setup);

        if (FMLEnvironment.dist.isClient()) {
            EnergizerClient.init(bus);
        }

        Energizer.init();
    }

    void register(RegisterEvent event) {
        // Registering Stamina attribute
        event.register(Registries.ATTRIBUTE, Energizer.id("generic.stamina"),() -> ModObjects.STAMINA_ATTRIBUTE);

        // Registering Vigor effect
        event.register(Registries.MOB_EFFECT, Energizer.id("vigor"),() -> ModObjects.VIGOR);

        // Registering Vigor potion
        event.register(Registries.POTION, Energizer.id("vigor_potion"), () -> ModObjects.VIGOR_POTION);

        // Registering Vigor long potion
        event.register(Registries.POTION, new ResourceLocation(Energizer.MOD_ID, "vigor_potion_long"),() -> ModObjects.VIGOR_POTION_LONG);
    }

    void setup(FMLCommonSetupEvent event) {
        Energizer.addRecipes();
    }

}