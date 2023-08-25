package net.kalandoz.runic_sword_art.capability;

import net.kalandoz.runic_sword_art.RunicSwordArt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
@Mod.EventBusSubscriber(modid = RunicSwordArt.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ModCapabilities {
    @CapabilityInject(IMana.class)
    public static Capability<IMana> MANA_CAP = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IMana.class, new ManaStorage(), Mana::new);
    }
    @SubscribeEvent
    public static void onStackAttachCapabilities(@Nonnull final AttachCapabilitiesEvent<Entity> e) {
        final Entity obj = e.getObject();
        if (obj instanceof PlayerEntity) {
            e.addCapability(new ResourceLocation(RunicSwordArt.MOD_ID, "mana"), new ManaProvider());
        }
    }
}
