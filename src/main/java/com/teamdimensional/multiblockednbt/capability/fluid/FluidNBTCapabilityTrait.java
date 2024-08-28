package com.teamdimensional.multiblockednbt.capability.fluid;

import com.cleanroommc.multiblocked.api.capability.trait.SingleCapabilityTrait;
import com.google.gson.JsonParser;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FluidNBTCapabilityTrait extends SingleCapabilityTrait {
    private final IStorageProcessCapabilityFluid storageProcessInventory;

    public FluidNBTCapabilityTrait() {
        super(FluidNBTMultiblockCapability.INSTANCE);
        storageProcessInventory = new StorageProcessImplementationFluid() {
            @Override
            public void setItem(FluidStack stack, boolean simulate) {
                super.setItem(stack, simulate);
                markAsDirty();
            }
        };
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        String str = compound.getString("d");
        serialize(new JsonParser().parse(str));
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setString("d", deserialize().toString());
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability != StorageProcessStorageFluid.STORAGE_PROCESS_CAPABILITY) return null;
        return (T) storageProcessInventory;
    }

    @Override
    public boolean hasUpdate() {
        return true;
    }
}
