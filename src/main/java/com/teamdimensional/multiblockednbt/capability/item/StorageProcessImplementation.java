package com.teamdimensional.multiblockednbt.capability.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.CapabilityManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class StorageProcessImplementation implements IStorageProcessCapability {
    private static final Factory FACTORY = new Factory();
    private static final StorageProcessStorage STORAGE = new StorageProcessStorage();

    private static class TwoStacks {
        ItemStack real = ItemStack.EMPTY;
        ItemStack simulated = ItemStack.EMPTY;
    }

    private final Map<String, TwoStacks> stacks = new HashMap<>();

    @Override
    public Collection<String> getKeys() {
        return stacks.keySet();
    }

    @Override
    public void setItem(String key, ItemStack stack, boolean simulate) {
        if (!stacks.containsKey(key)) stacks.put(key, new TwoStacks());
        TwoStacks t = stacks.get(key);
        if (!simulate) t.real = stack;
        else t.simulated = stack;
    }

    @Override
    public ItemStack getItem(String key, boolean simulate) {
        if (!stacks.containsKey(key)) return ItemStack.EMPTY;
        if (!simulate) return stacks.get(key).real;
        else return stacks.get(key).simulated;
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
