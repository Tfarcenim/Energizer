package com.gaura.energizer.utils;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;

public class StopSprintS2CPacket {

    public static void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {

        if (client.player != null) {

            ((IPlayerEntity) client.player).getStopSprint().putBoolean("stopSprint", buf.readBoolean());
        }
    }
}
