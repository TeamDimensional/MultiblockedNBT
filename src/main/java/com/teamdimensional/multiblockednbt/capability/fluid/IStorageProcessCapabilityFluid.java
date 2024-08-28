package com.teamdimensional.multiblockednbt.capability.fluid;

import net.minecraftforge.fluids.FluidStack;

public interface IStorageProcessCapabilityFluid {
    void setItem(FluidStack stack, boolean simulate);
    FluidStack getItem(boolean simulate);
}
