package net.kalandoz.runic_sword_art.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class AoEUtils {

    public static final Random RANDOM = new Random();

    public static void applyFlameBurstToNearbyEnemies(PlayerEntity playerIn, float distance) {
        applyToNearbyEntities(playerIn.getPosition(), playerIn.getEntityWorld(), distance, getCanApplyToEnemyPredicate(playerIn), (LivingEntity nearbyEntity) -> nearbyEntity.setFire(8));
    }

    public static void applyToNearbyEntities(BlockPos origin, World world, float distance, Predicate<LivingEntity> applicablePredicate, Consumer<LivingEntity> entityConsumer) {
        List<LivingEntity> nearbyEntities = getNearbyEnemies(origin, distance, world, applicablePredicate);
        if (!nearbyEntities.isEmpty()) {
            for (int i = 0; i < nearbyEntities.size(); i++) {
                if (nearbyEntities.size() >= i + 1) {
                    LivingEntity nearbyEntity = nearbyEntities.get(i);
                    entityConsumer.accept(nearbyEntity);
                }
            }
        }
    }

    public static List<LivingEntity> getNearbyEnemies(BlockPos origin, float distance, World world, Predicate<LivingEntity> applicablePredicate) {
        return world.getLoadedEntitiesWithinAABB(LivingEntity.class,
                new AxisAlignedBB(origin).grow(distance),
                applicablePredicate);
    }

    public static Predicate<LivingEntity> getCanApplyToEnemyPredicate(LivingEntity attacker) {
        return (nearbyEntity) -> AbilityUtils.canApplyToEnemy(attacker, nearbyEntity);
    }
}
