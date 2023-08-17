package net.kalandoz.runic_sword_art.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ManaProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundNBT> {

    private final ManaCapability instance = new ManaCapability();

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ModCapabilities.MANA_CAP) {
            return ModCapabilities.MANA_CAP.orEmpty(cap, LazyOptional.of(() -> instance));
        }
        return null;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = (CompoundNBT) ModCapabilities.MANA_CAP.getStorage()
                .writeNBT(ModCapabilities.MANA_CAP, instance, null);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        ModCapabilities.MANA_CAP.getStorage().readNBT(ModCapabilities.MANA_CAP, this.instance, null, nbt);
    }
}
