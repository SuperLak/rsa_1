package net.kalandoz.runic_sword_art.utils;

import net.minecraft.entity.LivingEntity;

public final class AbilityUtils {
    public static boolean canApplyToEnemy(LivingEntity attacker, LivingEntity nearbyEntity) {
        return nearbyEntity != attacker;
    }
}
