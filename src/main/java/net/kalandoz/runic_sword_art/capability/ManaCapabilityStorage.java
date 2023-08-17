package net.kalandoz.runic_sword_art.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/*
    ManaCapabilityStorage: Kinda confusing, mainly used when other mods want to "mesh" with this mod.
                           Both methods can return "null" optionally.
 */

public class ManaCapabilityStorage implements Capability.IStorage<IManaCapability>{
    @Nullable
    @Override
    public INBT writeNBT(Capability<IManaCapability> capability, IManaCapability instance, Direction side) {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putFloat("mana", instance.getMana());

        return nbt;
    }

    @Override
    public void readNBT(Capability<IManaCapability> capability, IManaCapability instance, Direction side, INBT nbt) {
        CompoundNBT nbt2 = (CompoundNBT) nbt;

        instance.set(nbt2.getFloat("mana"));
    }
}
