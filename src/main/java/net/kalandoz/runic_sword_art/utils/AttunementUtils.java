package net.kalandoz.runic_sword_art.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.kalandoz.runic_sword_art.capability.ModCapabilities;
import net.kalandoz.runic_sword_art.capability.attunement.IAttunement;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class AttunementUtils {
    private static final Map<Direction, LazyOptional<IAttunement>> cache = new HashMap<>();
    public static int primaryTimeCounter = 0;
    public static int secondaryTimeCounter = 0;

    public static LazyOptional<IAttunement> checkGuardianExistence(Direction direction, PlayerEntity player) {
        LazyOptional<IAttunement> targetCapability = cache.get(direction);

        if (targetCapability == null) {
            System.out.println("Guardian attunement is null!");
            targetCapability = player.getCapability(ModCapabilities.GUARDIAN_CAP, null);
            cache.put(direction, targetCapability);
            targetCapability.addListener(self -> cache.put(direction, null));
        }
        return targetCapability;
    }

    public static void primaryAbility(NetworkEvent.Context context) {
        ServerPlayerEntity playerIn = context.getSender();
        if (playerIn != null) {
            LazyOptional<IAttunement> guardianCapability = checkGuardianExistence(null, playerIn);

            guardianCapability.ifPresent(attunement -> attunement.primaryAbility(context));
        }
    }

    public static void secondaryAbility(NetworkEvent.Context context) {
        ServerPlayerEntity playerIn = context.getSender();
        LazyOptional<IAttunement> guardianCapability = checkGuardianExistence(null, playerIn);

        guardianCapability.ifPresent(attunement -> attunement.secondaryAbility(context));
    }

    public static void compositeAbility(NetworkEvent.Context context) {
        ServerPlayerEntity playerIn = context.getSender();
        LazyOptional<IAttunement> guardianCapability = checkGuardianExistence(null, playerIn);

        guardianCapability.ifPresent(attunement -> attunement.compositeAbility(context));
    }

    public static void displayTotal(PlayerEntity player, FontRenderer renderer, MatrixStack mStack, int width, int height) {
        LazyOptional<IAttunement> targetCapability = checkGuardianExistence(null, player);

        // color red
        System.out.println("Displaying Total!");
        targetCapability.ifPresent(attunement -> {
            System.out.println("Attunement is valid!");
            renderer.drawText(mStack, new StringTextComponent(attunement.getManaPrimary().getType() + " : " + attunement.getManaPrimary().getMana()), width, height, 0xFF66E2);
            renderer.drawText(mStack, new StringTextComponent(attunement.getManaSecondary().getType() + " : " + attunement.getManaSecondary().getMana()), width, height + 10, 0x80FF53);
        });
    }

    public static boolean consumePrimaryMana(Direction direction, PlayerEntity player, int points) {
        LazyOptional<IAttunement> targetCapability = checkGuardianExistence(direction, player);
        AtomicBoolean enough = new AtomicBoolean(false);

        targetCapability.ifPresent(attunement -> {
            attunement.getManaPrimary().consume(points);
            if (attunement.getManaPrimary().getMana() > 0.0F) {
                enough.set(true);
            }
        });
        return enough.get();
    }

    public static boolean consumeSecondaryMana(Direction direction, PlayerEntity player, int points) {
        LazyOptional<IAttunement> targetCapability = checkGuardianExistence(direction, player);
        AtomicBoolean enough = new AtomicBoolean(false);

        targetCapability.ifPresent(attunement -> {
            attunement.getManaSecondary().consume(points);
            if (attunement.getManaSecondary().getMana() > 0.0F) {
                enough.set(true);
            }
        });
        return enough.get();
    }


    public static void fillPrimaryMana(Direction direction, PlayerEntity player, int points) {
        LazyOptional<IAttunement> targetCapability = checkGuardianExistence(direction, player);

        targetCapability.ifPresent(attunement -> attunement.getManaPrimary().fill(points));
    }

    public static void fillSecondaryMana(Direction direction, PlayerEntity player, int points) {
        LazyOptional<IAttunement> targetCapability = checkGuardianExistence(direction, player);

        targetCapability.ifPresent(attunement -> attunement.getManaSecondary().fill(points));
    }

    public static void fillPrimaryManaProportionally(Direction direction, PlayerEntity player, int seconds) {
        // regenerates the player's mana in the number of ticks provided
        LazyOptional<IAttunement> targetCapability = checkGuardianExistence(direction, player);

        targetCapability.ifPresent(attunement -> {
            if (attunement.getManaPrimary().getMana() / seconds < 1) {
                System.out.println("Less than!");
                if (primaryTimeCounter * attunement.getManaPrimary().getMaxMana() >= seconds) {
                    System.out.println("More than!");
                    attunement.getManaPrimary().fill(1);
                    primaryTimeCounter = 0;
                } else {
                    primaryTimeCounter++;
                }
            } else {
                attunement.getManaPrimary().fill(attunement.getManaPrimary().getMaxMana() / seconds);
            }
        });
    }

    public static void fillSecondaryManaProportionally(Direction direction, PlayerEntity player, int seconds) {
        // regenerates the player's mana in the number of ticks provided
        LazyOptional<IAttunement> targetCapability = checkGuardianExistence(direction, player);

        targetCapability.ifPresent(attunement -> {
            if (attunement.getManaSecondary().getMaxMana() / seconds < 1) {
                if (secondaryTimeCounter * attunement.getManaSecondary().getMaxMana() >= seconds) {
                    attunement.getManaSecondary().fill(1);
                    secondaryTimeCounter = 0;
                } else {
                    secondaryTimeCounter++;
                }
            } else {
                attunement.getManaSecondary().fill(attunement.getManaSecondary().getMaxMana() / seconds);
            }
        });
    }

    public static void fillAllManaProportionally(Direction direction, PlayerEntity player, int seconds) {
        fillPrimaryManaProportionally(direction, player, seconds);
        fillSecondaryManaProportionally(direction, player, seconds);
    }
}
