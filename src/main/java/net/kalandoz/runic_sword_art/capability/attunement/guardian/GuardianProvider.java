package net.kalandoz.runic_sword_art.capability.attunement.guardian;

import net.kalandoz.runic_sword_art.capability.ModCapabilities;
import net.kalandoz.runic_sword_art.capability.attunement.IAttunement;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GuardianProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundNBT> {

    private final IAttunement instance = ModCapabilities.GUARDIAN_CAP.getDefaultInstance();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return ModCapabilities.GUARDIAN_CAP.orEmpty(cap, LazyOptional.of(() -> instance));
    }

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) ModCapabilities.GUARDIAN_CAP.getStorage()
                .writeNBT(ModCapabilities.GUARDIAN_CAP, instance, null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        ModCapabilities.GUARDIAN_CAP.getStorage().readNBT(ModCapabilities.GUARDIAN_CAP, this.instance, null, nbt);
    }
}
