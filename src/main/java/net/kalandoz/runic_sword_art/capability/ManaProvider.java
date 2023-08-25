package net.kalandoz.runic_sword_art.capability;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ManaProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundNBT> {

    private final IMana instance = ModCapabilities.MANA_CAP.getDefaultInstance();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return ModCapabilities.MANA_CAP.orEmpty(cap, LazyOptional.of(() -> instance));
    }

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) ModCapabilities.MANA_CAP.getStorage()
                .writeNBT(ModCapabilities.MANA_CAP, instance, null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        ModCapabilities.MANA_CAP.getStorage().readNBT(ModCapabilities.MANA_CAP, this.instance, null, nbt);
    }
}
