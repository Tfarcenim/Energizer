package com.gaura.energizer.platform;

import com.gaura.energizer.PacketHandlerForge;
import com.gaura.energizer.TomlConfig;
import com.gaura.energizer.network.C2SModPacket;
import com.gaura.energizer.network.S2CModPacket;
import com.gaura.energizer.platform.services.IPlatformHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.function.Function;

public class ForgePlatformHelper implements IPlatformHelper {
    final MLConfig config = new TomlConfig();

    @Override
    public String getPlatformName() {

        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    int i;

    @Override
    public <MSG extends S2CModPacket> void registerClientPacket(Class<MSG> packetLocation, Function<FriendlyByteBuf, MSG> reader) {
        PacketHandlerForge.INSTANCE.registerMessage(i++, packetLocation, MSG::write, reader, PacketHandlerForge.wrapS2C());
    }

    @Override
    public <MSG extends C2SModPacket> void registerServerPacket(Class<MSG> packetLocation, Function<FriendlyByteBuf, MSG> reader) {
        PacketHandlerForge.INSTANCE.registerMessage(i++, packetLocation, MSG::write, reader, PacketHandlerForge.wrapC2S());
    }


    @Override
    public void sendToClient(S2CModPacket msg, ServerPlayer player) {
        if (player.connection != null) {
            PacketHandlerForge.sendToClient(msg, player);
        }
    }

    @Override
    public void sendToServer(C2SModPacket msg) {
        PacketHandlerForge.sendToServer(msg);
    }

    @Override
    public MLConfig getConfig() {
        return config;
    }

    @Override
    public int getBaseYOffset() {
        return ((ForgeGui)Minecraft.getInstance().gui).rightHeight;
    }

    @Override
    public void offsetY() {
        ((ForgeGui)Minecraft.getInstance().gui).rightHeight +=10;
    }
}