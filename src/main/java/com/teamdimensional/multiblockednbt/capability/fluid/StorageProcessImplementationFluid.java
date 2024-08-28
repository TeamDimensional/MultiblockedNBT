package com.teamdimensional.multiblockednbt.capability.fluid;

import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.concurrent.Callable;

public class StorageProcessImplementationFluid implements IStorageProcessCapabilityFluid {
    private static final Factory FACTORY = new Factory();
    private static final StorageProcessStorageFluid STORAGE = new StorageProcessStorageFluid();

    private FluidStack stack = new FluidStack(FluidRegistry.WATER, 0);
    private FluidStack stackSimulate = new FluidStack(FluidRegistry.WATER, 0);

    @Override
    public void setItem(FluidStack stack, boolean simulate) {
        if (!simulate) this.stack = stack;
        else this.stackSimulate = stack;
    }

    @Override
    public FluidStack getItem(boolean simulate) {
        if (!simulate) return stack;
        else return this.stackSimulate;
    }

    private static class Factory implements Callable<IStorageProcessCapabilityFluid> {
        @Override
        public IStorageProcessCapabilityFluid call() {
            return new StorageProcessImplementationFluid();
        }
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(IStorageProcessCapabilityFluid.class, STORAGE, FACTORY);
    }
}
