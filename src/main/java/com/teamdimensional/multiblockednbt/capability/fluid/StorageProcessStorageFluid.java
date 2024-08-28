package com.teamdimensional.multiblockednbt.capability.fluid;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class StorageProcessStorageFluid implements Capability.IStorage<IStorageProcessCapabilityFluid> {
    @CapabilityInject(IStorageProcessCapabilityFluid.class)
    public static Capability<IStorageProcessCapabilityFluid> STORAGE_PROCESS_CAPABILITY = null;

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<IStorageProcessCapabilityFluid> capability, IStorageProcessCapabilityFluid instance, EnumFacing side) {
        NBTTagCompound simulated = new NBTTagCompound(), real = new NBTTagCompound();
        instance.getItem(true).writeToNBT(simulated);
        instance.getItem(false).writeToNBT(real);
        NBTTagCompound output = new NBTTagCompound();
        output.setTag("simulated", simulated);
        output.setTag("real", real);
        return output;
    }

    @Override
    public void readNBT(Capability<IStorageProcessCapabilityFluid> capability, IStorageProcessCapabilityFluid instance, EnumFacing side, NBTBase nbt) {
        NBTTagCompound comp = (NBTTagCompound) nbt;
        FluidStack real = FluidStack.loadFluidStackFromNBT((NBTTagCompound) comp.getTag("real"));
        FluidStack simulated = FluidStack.loadFluidStackFromNBT((NBTTagCompound) comp.getTag("simulated"));
        instance.setItem(real, false);
        instance.setItem(simulated, true);
    }
}
