package com.gaura.energizer.platform;

public enum ModCompat {
    heartymeals;

    public final boolean loaded;
    ModCompat() {
        loaded = Services.PLATFORM.isModLoaded(name());
    }
}
