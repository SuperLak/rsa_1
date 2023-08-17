package net.kalandoz.runic_sword_art.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class ModCapabilities {
    @CapabilityInject(IManaCapability.class)
    public static Capability<IManaCapability> MANA_CAP = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IManaCapability.class, new ManaCapabilityStorage(), ManaCapability::new);
    }
}
