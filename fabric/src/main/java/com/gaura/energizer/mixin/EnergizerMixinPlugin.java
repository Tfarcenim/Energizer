package com.gaura.energizer.mixin;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class EnergizerMixinPlugin implements IMixinConfigPlugin {
    public static final String NOSTALGIC_TWEAKS_MOD_ID = "nostalgic_tweaks";

    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() {

        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {

        if (FabricLoader.getInstance().isModLoaded(NOSTALGIC_TWEAKS_MOD_ID)) {

            return !mixinClassName.equals("com.gaura.energizer.mixin.GuiMixinFabric");
        }

        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {

        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
