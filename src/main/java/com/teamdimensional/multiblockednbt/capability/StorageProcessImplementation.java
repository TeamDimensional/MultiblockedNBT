package com.teamdimensional.multiblockednbt.capability;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.CapabilityManager;

import java.util.concurrent.Callable;

public class StorageProcessImplementation implements IStorageProcessCapability {
    private static final Factory FACTORY = new Factory();
    private static final StorageProcessStorage STORAGE = new StorageProcessStorage();

    private ItemStack stack = ItemStack.EMPTY;
    private ItemStack stackSimulate = ItemStack.EMPTY;

    @Override
    public void setItem(ItemStack stack, boolean simulate) {
        if (!simulate) this.stack = stack;
        else this.stackSimulate = stack;
    }

    @Override
    public ItemStack getItem(boolean simulate) {
        if (!simulate) return stack;
        else return this.stackSimulate;
    }

    private static class Factory implements Callable<IStorageProcessCapability> {
        @Override
        public IStorageProcessCapability call() {
            return new StorageProcessImplementation();
        }
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(IStorageProcessCapability.class, STORAGE, FACTORY);
    }
}
