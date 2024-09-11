package com.gaura.energizer.init;

import com.gaura.energizer.Energizer;
import com.gaura.energizer.effect.VigorEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class ModObjects {
    // Stamina attribute
    public static final Attribute STAMINA_ATTRIBUTE = new RangedAttribute("attribute.name." + Energizer.MOD_ID + ".player.stamina", 20, 1, 1024).setSyncable(true);
    // Vigor effect
    public static final VigorEffect VIGOR = new VigorEffect(MobEffectCategory.BENEFICIAL, 0x14B2FF);
}
