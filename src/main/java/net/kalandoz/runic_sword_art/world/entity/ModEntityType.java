package net.kalandoz.runic_sword_art.world.entity;

import net.kalandoz.runic_sword_art.RunicSwordArt;
import net.kalandoz.runic_sword_art.world.entity.projectile.FlameArcEntity;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityType {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITIES, RunicSwordArt.MOD_ID);

    public static final RegistryObject<EntityType<FlameArcEntity>> FLAME_ARC = ENTITIES.register("flame_arc",
            () ->EntityType.Builder.<FlameArcEntity>create(
                    FlameArcEntity::new, EntityClassification.MISC).size(1f, 1f)
                    .trackingRange(4).updateInterval(10)
                    .build(new ResourceLocation(RunicSwordArt.MOD_ID, "textures/entity/projectile").toString()));

    private static <T extends Entity> EntityType<T> register(String key, EntityType.Builder<T> builder) {
        return Registry.register(Registry.ENTITY_TYPE, key, builder.build(key));
    }

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
