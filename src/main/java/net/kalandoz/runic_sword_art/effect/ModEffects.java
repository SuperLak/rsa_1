package net.kalandoz.runic_sword_art.effect;

import net.kalandoz.runic_sword_art.RunicSwordArt;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEffects {
    public static final DeferredRegister<Effect> EFFECTS
            = DeferredRegister.create(ForgeRegistries.POTIONS, RunicSwordArt.MOD_ID);

    public static final RegistryObject<Effect> BURNING = EFFECTS.register("burning",
            () -> new BurningEffect(EffectType.HARMFUL, 3124687));

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
