package net.kalandoz.runic_sword_art.world.entity.projectile;

import net.kalandoz.runic_sword_art.effect.ModEffects;
import net.kalandoz.runic_sword_art.world.entity.ModEntityType;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

@SuppressWarnings("EntityConstructor")
public class FlameArcEntity extends DamagingProjectileEntity {
    public FlameArcEntity(EntityType<? extends FlameArcEntity> flameArcEntity, World worldIn) {
    super(flameArcEntity, worldIn);
}

    @OnlyIn(Dist.CLIENT)
    public FlameArcEntity(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(ModEntityType.FLAME_ARC.get(), x, y, z, accelX, accelY, accelZ, worldIn);
    }

    public FlameArcEntity(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(ModEntityType.FLAME_ARC.get(), shooter, accelX, accelY, accelZ, worldIn);
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    protected void onImpact(RayTraceResult result) {
        super.onImpact(result);
        Entity entity = this.getShooter();
        if (result.getType() != RayTraceResult.Type.ENTITY || !((EntityRayTraceResult)result).getEntity().isEntityEqual(entity)) {
            if (!this.world.isRemote) {
                List<LivingEntity> list = this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(4.0D, 2.0D, 4.0D));
                AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ());
                if (entity instanceof LivingEntity) {
                    areaEffectCloudEntity.setOwner((LivingEntity)entity);
                }

                areaEffectCloudEntity.setParticleData(ParticleTypes.FLAME);
                areaEffectCloudEntity.setRadius(3.0F);
                areaEffectCloudEntity.setDuration(600);
                areaEffectCloudEntity.setRadiusPerTick((7.0F - areaEffectCloudEntity.getRadius()) / (float)areaEffectCloudEntity.getDuration());
                areaEffectCloudEntity.addEffect(new EffectInstance(ModEffects.BURNING.get(), 1, 1));
                if (!list.isEmpty()) {
                    for(LivingEntity livingentity : list) {
                        double d0 = this.getDistanceSq(livingentity);
                        if (d0 < 16.0D) {
                            areaEffectCloudEntity.setPosition(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ());
                            break;
                        }
                    }
                }

                this.world.playEvent(2006, this.getPosition(), this.isSilent() ? -1 : 1);
                this.world.addEntity(areaEffectCloudEntity);
                this.remove();
            }

        }
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith() {
        return false;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    protected IParticleData getParticle() {
        return ParticleTypes.FLAME;
    }

    protected boolean isFireballFiery() {
        return false;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
