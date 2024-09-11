package com.gaura.energizer.network;

import com.gaura.energizer.IPlayerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

public class S2CSetStaminaPacket implements S2CModPacket{

    final float stamina;

    public S2CSetStaminaPacket(float stamina) {
        this.stamina = stamina;
    }

    public S2CSetStaminaPacket(FriendlyByteBuf buf) {
        this(buf.readFloat());
    }

    @Override
    public void handleClient() {
        ((IPlayerEntity) Minecraft.getInstance().player).setStamina(stamina);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeFloat(stamina);
    }
}
