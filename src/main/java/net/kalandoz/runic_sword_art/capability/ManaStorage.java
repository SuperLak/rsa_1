package net.kalandoz.runic_sword_art.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/*
    ManaCapabilityStorage: Kinda confusing, mainly used to store our capability through serializing and deserializing
    our capability
 */

public class ManaStorage implements Capability.IStorage<IMana>{
    @Nullable
    @Override
    public INBT writeNBT(Capability<IMana> capability, IMana instance, Direction side) {
        final CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("mana", instance.getMana());
        return nbt;
    }

    @Override
    public void readNBT(Capability<IMana> capability, IMana instance, Direction side, INBT nbt) {
        CompoundNBT nbt1 = (CompoundNBT) nbt;
        instance.set(nbt1.getInt("mana"));
    }
}
