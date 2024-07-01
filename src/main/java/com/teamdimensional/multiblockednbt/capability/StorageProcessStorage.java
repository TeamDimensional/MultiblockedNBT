package com.teamdimensional.multiblockednbt.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;

public class StorageProcessStorage implements Capability.IStorage<IStorageProcessCapability> {
    @CapabilityInject(IStorageProcessCapability.class)
    public static Capability<IStorageProcessCapability> STORAGE_PROCESS_CAPABILITY = null;

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<IStorageProcessCapability> capability, IStorageProcessCapability instance, EnumFacing side) {
        NBTTagCompound simulated = instance.getItem(true).serializeNBT();
        NBTTagCompound real = instance.getItem(false).serializeNBT();
        NBTTagCompound output = new NBTTagCompound();
        output.setTag("simulated", simulated);
        output.setTag("real", real);
        return output;
    }

    @Override
    public void readNBT(Capability<IStorageProcessCapability> capability, IStorageProcessCapability instance, EnumFacing side, NBTBase nbt) {
        ItemStack real = ItemStack.EMPTY, simulated = ItemStack.EMPTY;
        NBTTagCompound comp = (NBTTagCompound) nbt;
        real.deserializeNBT((NBTTagCompound) comp.getTag("real"));
        simulated.deserializeNBT((NBTTagCompound) comp.getTag("simulated"));
        instance.setItem(real, false);
        instance.setItem(simulated, true);
    }
}
