package net.kalandoz.runic_sword_art.capability.attunement.guardian;

import net.kalandoz.runic_sword_art.capability.attunement.IAttunement;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/*
    ManaCapabilityStorage: Kinda confusing, mainly used to store our capability through serializing and deserializing
    our capability
 */

public class GuardianStorage implements Capability.IStorage<IAttunement>{
    @Nullable
    @Override
    public INBT writeNBT(Capability<IAttunement> capability, IAttunement instance, Direction side) {
        final CompoundNBT nbt = new CompoundNBT();
        nbt.putString("primary_type", instance.getManaPrimary().getType());
        nbt.putInt("primary_mana", instance.getManaPrimary().getMana());
        nbt.putInt("primary_max", instance.getManaPrimary().getMaxMana());
        nbt.putInt("primary_color", instance.getManaPrimary().getColor());
        nbt.putString("secondary_type", instance.getManaSecondary().getType());
        nbt.putInt("secondary_mana", instance.getManaSecondary().getMana());
        nbt.putInt("secondary_max", instance.getManaSecondary().getMaxMana());
        nbt.putInt("secondary_color", instance.getManaSecondary().getColor());
        nbt.putInt("level", instance.getLevel());
        return nbt;
    }

    @Override
    public void readNBT(Capability<IAttunement> capability, IAttunement instance, Direction side, INBT nbt) {
        CompoundNBT nbt1 = (CompoundNBT) nbt;
        instance.setPrimary(nbt1.getInt("primary_mana"),  nbt1.getInt("primary_max"));
        instance.setSecondary(nbt1.getInt("secondary_mana"), nbt1.getInt("secondary_max"));
        instance.setLevel(nbt1.getInt("level"));
    }
}
