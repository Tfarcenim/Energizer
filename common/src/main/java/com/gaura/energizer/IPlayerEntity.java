package com.gaura.energizer;

public interface IPlayerEntity {

    boolean getStopSprint();
    void setStopSprint(boolean stopSprint);
    float getStamina();
    void setStamina(float stamina);
    float getMaxStamina();
    long getLastStaminaLossTime();
    void setLastStaminaLossTIme(long time);
}
