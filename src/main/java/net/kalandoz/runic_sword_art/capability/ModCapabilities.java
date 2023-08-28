package net.kalandoz.runic_sword_art.capability;

import net.kalandoz.runic_sword_art.RunicSwordArt;
import net.kalandoz.runic_sword_art.capability.attunement.IAttunement;
import net.kalandoz.runic_sword_art.capability.attunement.guardian.GuardianStorage;
import net.kalandoz.runic_sword_art.capability.attunement.guardian.Guardian;
import net.kalandoz.runic_sword_art.capability.attunement.guardian.GuardianProvider;
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
    @CapabilityInject(IAttunement.class)
    public static Capability<IAttunement> GUARDIAN_CAP = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IAttunement.class, new GuardianStorage(), () -> new Guardian(60));
    }
    @SubscribeEvent
    public static void onStackAttachCapabilities(@Nonnull final AttachCapabilitiesEvent<Entity> e) {
        final Entity obj = e.getObject();
        if (obj instanceof PlayerEntity) {
            e.addCapability(new ResourceLocation(RunicSwordArt.MOD_ID, "guardian"), new GuardianProvider());
        }
    }
}
