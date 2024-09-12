package com.gaura.energizer;

import com.gaura.energizer.platform.Services;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

public class EnergizerClient {

    public static void init(IEventBus bus) {
        bus.addListener(EnergizerClient::overlay);
        MinecraftForge.EVENT_BUS.addListener(EnergizerClient::disableHunger);
    }

    static void disableHunger(RenderGuiOverlayEvent event) {
        if (event.getOverlay() == VanillaGuiOverlay.FOOD_LEVEL.type()&& Services.PLATFORM.getConfig().removeHunger()) {
            event.setCanceled(true);
        }
    }

    static void overlay(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.FOOD_LEVEL.id(),"stamina",STAMINA);
    }

    static IGuiOverlay STAMINA = (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> Huds.renderStamina(gui,guiGraphics,screenWidth,screenHeight);

}
