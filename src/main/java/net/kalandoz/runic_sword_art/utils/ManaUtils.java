package net.kalandoz.runic_sword_art.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.kalandoz.runic_sword_art.capability.IMana;
import net.kalandoz.runic_sword_art.capability.ModCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.LazyOptional;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class ManaUtils {
    private static final Map<Direction, LazyOptional<IMana>> cache = new HashMap<>();
    public static final Random rand = new Random();
    public static int timeCounter = 0;

    public static LazyOptional<IMana> checkExistence(Direction direction, PlayerEntity player) {
        LazyOptional<IMana> targetCapability = cache.get(direction);

        if (targetCapability == null) {
            targetCapability = player.getCapability(ModCapabilities.MANA_CAP, null);
            cache.put(direction, targetCapability);
            targetCapability.addListener(self -> cache.put(direction, null));
        }
        return targetCapability;
    }

    public static void printTotal(Direction direction, PlayerEntity player) {
        LazyOptional<IMana> targetCapability = checkExistence(direction, player);

        targetCapability.ifPresent(mana -> Minecraft.getInstance().player.sendMessage(new StringTextComponent(
                    "Total Mana: " + mana.getMana()), null));
    }

    public static void displayTotal(PlayerEntity player, FontRenderer renderer, MatrixStack mStack, int width, int height) {
        LazyOptional<IMana> targetCapability = checkExistence(null, player);

        // color red
        targetCapability.ifPresent(mana -> renderer.drawText(mStack, new StringTextComponent("Total Mana: " + mana.getMana()), width, height, 0xFF0000));
    }

    public static boolean consumeMana(Direction direction, PlayerEntity player, int points) {
        LazyOptional<IMana> targetCapability = checkExistence(direction, player);
        AtomicBoolean enough = new AtomicBoolean(false);

        targetCapability.ifPresent(mana -> {
            mana.consume(points);
            if (mana.getMana() > 0.0F) {
                enough.set(true);
            }
        });
        return enough.get();
    }

    public static void fillMana(Direction direction, PlayerEntity player, int points) {
        LazyOptional<IMana> targetCapability = checkExistence(direction, player);

        targetCapability.ifPresent(mana -> mana.fill(points));
    }

    public static void fillManaProportionally(Direction direction, PlayerEntity player, int seconds) {
        // regenerates the player's mana in the number of ticks provided
        LazyOptional<IMana> targetCapability = checkExistence(direction, player);

        targetCapability.ifPresent(mana -> {
            if (mana.getMax() / seconds < 1) {
                if (timeCounter * mana.getMax() >= seconds) {
                    System.out.println("Started deducting!");
                    mana.fill(1);
                    timeCounter = 0;
                } else {
                    System.out.println("Couldn't deducted! Time Counter: " + timeCounter);
                    timeCounter++;
                }
            } else {
                mana.fill(mana.getMax() / seconds);
            }
        });
    }
}
