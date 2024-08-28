package com.teamdimensional.multiblockednbt.capability.item;

import net.minecraft.item.ItemStack;

import java.util.Collection;

public interface IStorageProcessCapability {
    Collection<String> getKeys();
    void setItem(String key, ItemStack stack, boolean simulate);
    ItemStack getItem(String key, boolean simulate);
}
