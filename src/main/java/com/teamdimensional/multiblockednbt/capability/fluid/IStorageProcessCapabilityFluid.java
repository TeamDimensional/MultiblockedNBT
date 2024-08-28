package com.teamdimensional.multiblockednbt.capability.fluid;

import net.minecraftforge.fluids.FluidStack;

import java.util.Collection;

public interface IStorageProcessCapabilityFluid {
    Collection<String> getKeys();
    void setItem(String key, FluidStack stack, boolean simulate);
    FluidStack getItem(String key, boolean simulate);
}
