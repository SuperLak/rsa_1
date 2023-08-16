package net.kalandoz.runic_sword_art.effect;

import net.kalandoz.runic_sword_art.item.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;

public class BurningEffect extends Effect {
    public BurningEffect(EffectType mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public void performEffect(LivingEntity livingEntity, int pAmplifier) {
        if (this == ModEffects.BURNING.get()) {
            if (livingEntity.isInWater() || livingEntity.getHeldItemMainhand().getItem() == ModItems.FLAME_SWORD.get()) {
                livingEntity.removePotionEffect(ModEffects.BURNING.get());
            }
            if (!livingEntity.isImmuneToFire()) {
                livingEntity.setFire(1);
                livingEntity.attackEntityFrom(DamageSource.ON_FIRE, 1.0F);
            }
        }
        super.performEffect(livingEntity, pAmplifier);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }
}
