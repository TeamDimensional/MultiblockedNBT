package com.teamdimensional.multiblockednbt.capability.fluid;

import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class StorageProcessImplementationFluid implements IStorageProcessCapabilityFluid {
    private static final Factory FACTORY = new Factory();
    private static final StorageProcessStorageFluid STORAGE = new StorageProcessStorageFluid();

    private static final FluidStack EMPTY = new FluidStack(FluidRegistry.WATER, 0);

    private static class TwoStacks {
        FluidStack real = EMPTY;
        FluidStack simulated = EMPTY;
    }

    private final Map<String, TwoStacks> stacks = new HashMap<>();

    @Override
    public Collection<String> getKeys() {
        return stacks.keySet();
    }

    @Override
    public void setItem(String key, FluidStack stack, boolean simulate) {
        if (!stacks.containsKey(key)) stacks.put(key, new TwoStacks());
        TwoStacks t = stacks.get(key);
        if (!simulate) t.real = stack;
        else t.simulated = stack;
    }

    @Override
    public FluidStack getItem(String key, boolean simulate) {
        if (!stacks.containsKey(key)) return EMPTY;
        if (!simulate) return stacks.get(key).real;
        else return stacks.get(key).simulated;
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
