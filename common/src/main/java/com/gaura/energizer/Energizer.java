package com.gaura.energizer;

import com.gaura.energizer.config.HealFood;
import com.gaura.energizer.init.ModObjects;
import com.gaura.energizer.mixin.LivingEntityInvoker;
import com.gaura.energizer.network.PacketHandler;
import com.gaura.energizer.network.S2CSetStaminaPacket;
import com.gaura.energizer.network.S2CStopSprintPacket;
import com.gaura.energizer.platform.MLConfig;
import com.gaura.energizer.platform.ModCompat;
import com.gaura.energizer.platform.Services;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class Energizer {

    public static final String MOD_ID = "energizer";
    public static final String MOD_NAME = "Energizer";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
    public static final ResourceLocation STAMINA_ICONS = id("textures/stamina/stamina_icons.png");
    public static final HealFood[] default_heals = {

            new HealFood("minecraft:apple", 2),
            new HealFood("minecraft:baked_potato", 3),
            new HealFood("minecraft:beef", 1),
            new HealFood("minecraft:beetroot", 1),
            new HealFood("minecraft:beetroot_soup", 5),
            new HealFood("minecraft:bread", 3),
            new HealFood("minecraft:cake", 1),
            new HealFood("minecraft:carrot", 2),
            new HealFood("minecraft:chicken", 1),
            new HealFood("minecraft:chorus_fruit", 1),
            new HealFood("minecraft:cod", 1),
            new HealFood("minecraft:cooked_beef", 4),
            new HealFood("minecraft:cooked_chicken", 3),
            new HealFood("minecraft:cooked_cod", 3),
            new HealFood("minecraft:cooked_mutton", 3),
            new HealFood("minecraft:cooked_porkchop", 4),
            new HealFood("minecraft:cooked_rabbit", 3),
            new HealFood("minecraft:cooked_salmon", 3),
            new HealFood("minecraft:cookie", 1),
            new HealFood("minecraft:dried_kelp", 1),
            new HealFood("minecraft:enchanted_golden_apple", 4),
            new HealFood("minecraft:golden_apple", 3),
            new HealFood("minecraft:golden_carrot", 4),
            new HealFood("minecraft:honey_bottle", 2),
            new HealFood("minecraft:melon_slice", 1),
            new HealFood("minecraft:mushroom_stew", 5),
            new HealFood("minecraft:mutton", 1),
            new HealFood("minecraft:poisonous_potato", 1),
            new HealFood("minecraft:porkchop", 1),
            new HealFood("minecraft:potato", 1),
            new HealFood("minecraft:pufferfish", 1),
            new HealFood("minecraft:pumpkin_pie", 5),
            new HealFood("minecraft:rabbit", 1),
            new HealFood("minecraft:rabbit_stew", 5),
            new HealFood("minecraft:rotten_flesh", 1),
            new HealFood("minecraft:salmon", 1),
            new HealFood("minecraft:spider_eye", 1),
            new HealFood("minecraft:suspicious_stew", 5),
            new HealFood("minecraft:sweet_berries", 1),
            new HealFood("minecraft:glow_berries", 1),
            new HealFood("minecraft:tropical_fish", 1)
    };

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {
        // It is common for all supported loaders to provide a similar feature that can not be used directly in the
        // common code. A popular way to get around this is using Java's built-in service loader feature to create
        // your own abstraction layer. You can learn more about this in our provided services class. In this example
        // we have an interface in the common code and use a loader specific implementation to delegate our call to
        // the platform specific approach.
        PacketHandler.registerPackets();
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static boolean removeHunger() {
        return !ModCompat.heartymeals.loaded && Services.PLATFORM.getConfig().removeHunger();
    }

    public static void playerTick(Player player) {
        IPlayerEntity iPlayer = (IPlayerEntity) player;
        long currentTime = player.level().getGameTime();
        MLConfig config = Services.PLATFORM.getConfig();
        if (!player.isCreative() && !player.isSpectator() && !player.level().isClientSide && !(player.level().getDifficulty() == Difficulty.PEACEFUL && config.disableStaminaInPeaceful())) {

            boolean hasHunger = player.hasEffect(MobEffects.HUNGER);
            boolean stopSprint = iPlayer.getStopSprint();
            double staminaIncrease = (hasHunger && stopSprint) ? config.staminaIncreaseHungerEmpty() : hasHunger ? config.staminaIncreaseHunger() : stopSprint ? config.staminaIncreaseEmpty() : config.staminaIncrease();

            if (player.hasEffect(ModObjects.VIGOR)) {

                iPlayer.setStamina(iPlayer.getMaxStamina());
                iPlayer.setStopSprint(false);
            } else {

                if (player.isPassenger()) {

                    if ((iPlayer.getStamina() < iPlayer.getMaxStamina()) && ((currentTime - iPlayer.getLastStaminaLossTime()) >= config.staminaRegenerationDelay())) {

                        iPlayer.setStamina((float) (iPlayer.getStamina() + staminaIncrease));
                    }
                } else {

                    double staminaDecrease;
                    if (player.isSprinting() && !player.isUnderWater() && !stopSprint) {

                        staminaDecrease = hasHunger ? config.sprintingStaminaDecreaseHunger() : config.sprintingStaminaDecrease();
                        iPlayer.setStamina((float) (iPlayer.getStamina() - staminaDecrease));
                        iPlayer.setLastStaminaLossTIme(currentTime);

                        if (iPlayer.getStamina() <= 0.0F && config.disableSprintSwimEmptyStamina()) {

                            iPlayer.setStopSprint(true);
                        }
                    } else if (player.isSwimming() && player.isUnderWater() && config.swimmingCostStamina() && !stopSprint) {

                        staminaDecrease = hasHunger ? config.swimmingStaminaDecreaseHunger() : config.swimmingStaminaDecrease();
                        iPlayer.setStamina((float) (iPlayer.getStamina() - staminaDecrease));
                        iPlayer.setLastStaminaLossTIme(currentTime);

                        if (iPlayer.getStamina() <= 0.0F && config.disableSprintSwimEmptyStamina()) {

                            iPlayer.setStopSprint(true);
                        }
                    } else if ((iPlayer.getStamina() < iPlayer.getMaxStamina()) && ((currentTime - iPlayer.getLastStaminaLossTime()) >= config.staminaRegenerationDelay())) {

                        iPlayer.setStamina((float) (iPlayer.getStamina() + staminaIncrease));
                    } else if (iPlayer.getStamina() == iPlayer.getMaxStamina()) {

                        iPlayer.setStopSprint(false);
                    }
                }
            }
            Services.PLATFORM.sendToClient(new S2CStopSprintPacket(iPlayer.getStopSprint()), (ServerPlayer) player);
            Services.PLATFORM.sendToClient(new S2CSetStaminaPacket(iPlayer.getStamina()), (ServerPlayer) player);
        }
    }

    public static ItemStack onEaten(LivingEntity living, Level level, ItemStack stack) {

        if (stack.isEdible() && living instanceof ServerPlayer serverPlayer && !level.isClientSide()) {

            serverPlayer.awardStat(Stats.ITEM_USED.get(stack.getItem()));

            level.playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);

            ((LivingEntityInvoker) serverPlayer).invokeApplyFoodEffects(stack, level, serverPlayer);

            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);

            serverPlayer.heal(Utils.getHealAmount(stack));

            if (!serverPlayer.isCreative()) {

                stack.shrink(1);
            }

            serverPlayer.gameEvent(GameEvent.EAT);
        }
        return stack;
    }

}