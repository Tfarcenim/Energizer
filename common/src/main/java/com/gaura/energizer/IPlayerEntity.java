package com.gaura.energizer;

import net.minecraft.nbt.CompoundTag;

public interface IPlayerEntity {

    boolean getStopSprint();
    void setStopSprint(boolean stopSprint);
    float getStamina();
    void setStamina(float stamina);
}
