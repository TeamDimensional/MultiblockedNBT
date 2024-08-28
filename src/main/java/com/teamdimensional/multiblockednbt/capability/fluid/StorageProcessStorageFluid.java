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
        NBTTagCompound simulatedList = new NBTTagCompound(), realList = new NBTTagCompound();
        for (String s : instance.getKeys()) {
            NBTTagCompound simulated = new NBTTagCompound(), real = new NBTTagCompound();
            instance.getItem(s, true).writeToNBT(simulated);
            instance.getItem(s, false).writeToNBT(real);
            simulatedList.setTag(s, simulated);
            realList.setTag(s, real);
        }
        NBTTagCompound output = new NBTTagCompound();
        output.setTag("simulatedList", simulatedList);
        output.setTag("realList", realList);
        return output;
    }

    @Override
    public void readNBT(Capability<IStorageProcessCapabilityFluid> capability, IStorageProcessCapabilityFluid instance, EnumFacing side, NBTBase nbt) {
        NBTTagCompound comp = (NBTTagCompound) nbt;
        NBTTagCompound realList = (NBTTagCompound) comp.getTag("realList");
        for (String s : realList.getKeySet()) {
            FluidStack real = FluidStack.loadFluidStackFromNBT((NBTTagCompound) realList.getTag(s));
            instance.setItem(s, real, false);
        }
        NBTTagCompound simulatedList = (NBTTagCompound) comp.getTag("simulatedList");
        for (String s : simulatedList.getKeySet()) {
            FluidStack real = FluidStack.loadFluidStackFromNBT((NBTTagCompound) simulatedList.getTag(s));
            instance.setItem(s, real, true);
        }
    }
}
