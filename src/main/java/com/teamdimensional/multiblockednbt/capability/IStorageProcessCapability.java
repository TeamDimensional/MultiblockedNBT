package com.teamdimensional.multiblockednbt.capability;

import net.minecraft.item.ItemStack;

public interface IStorageProcessCapability {
    void setItem(ItemStack stack, boolean simulate);
    ItemStack getItem(boolean simulate);
}
