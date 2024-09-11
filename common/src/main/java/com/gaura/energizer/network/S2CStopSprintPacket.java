package com.gaura.energizer.network;

import com.gaura.energizer.IPlayerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

public class S2CStopSprintPacket implements S2CModPacket{

    final boolean stopSprint;

    public S2CStopSprintPacket(boolean stopSprint) {
        this.stopSprint = stopSprint;
    }

    public S2CStopSprintPacket(FriendlyByteBuf buf) {
        this(buf.readBoolean());
    }

    @Override
    public void handleClient() {
        ((IPlayerEntity) Minecraft.getInstance().player).setStopSprint(stopSprint);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeBoolean(stopSprint);
    }
}
