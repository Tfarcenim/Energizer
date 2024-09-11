package com.gaura.energizer.network;

import com.gaura.energizer.Energizer;
import com.gaura.energizer.platform.Services;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;

public class PacketHandler {

    public static void registerPackets() {
        Services.PLATFORM.registerClientPacket(S2CStopSprintPacket.class, S2CStopSprintPacket::new);
        Services.PLATFORM.registerClientPacket(S2CSetStaminaPacket.class, S2CSetStaminaPacket::new);
    }

    public static ResourceLocation packet(Class<?> clazz) {
        return Energizer.id(clazz.getName().toLowerCase(Locale.ROOT));
    }

}
