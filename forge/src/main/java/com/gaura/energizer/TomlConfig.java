package com.gaura.energizer;

import com.gaura.energizer.platform.MLConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class TomlConfig implements MLConfig {

    public static final Server SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;

    static {
        final Pair<Server, ForgeConfigSpec> specPair2 = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER_SPEC = specPair2.getRight();
        SERVER = specPair2.getLeft();
    }

    public static class Server {
        public static ForgeConfigSpec.BooleanValue removeHunger;

        public static ForgeConfigSpec.BooleanValue staminaBlink;
        public static ForgeConfigSpec.BooleanValue vigorWave;
        public static ForgeConfigSpec.BooleanValue sprintKeybind;
        public static ForgeConfigSpec.BooleanValue lowerJump;

        public Server(ForgeConfigSpec.Builder builder) {
            builder.push("hunger");
            removeHunger = builder.comment("If the hunger should be removed.").define("remove_hunger",true);
            builder.pop();
            builder.push("stamina");
            staminaBlink = builder.comment("If the stamina bar should blink when stamina is full.").define("stamina_blink",true);
            vigorWave = builder.comment("If the stamina bar should wave when having Vigor effect.").define("vigor_wave",true);
            sprintKeybind = builder.comment("If you must keep pressing the sprint keybind to sprint, rather than toggling it.").define("sprint_keybind",true);
            lowerJump = builder.comment("If the player will jump lower when the stamina bar is empty.").define("lower_jump",true);
            builder.pop();
        }
    }

    @Override
    public boolean removeHunger() {
        return Server.removeHunger.get();
    }

    @Override
    public boolean staminaBlink() {
        return Server.staminaBlink.get();
    }

    @Override
    public boolean sprintKeybind() {
        return Server.sprintKeybind.get();
    }

    @Override
    public boolean lowerJump() {
        return Server.lowerJump.get();
    }
}
