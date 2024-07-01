package com.teamdimensional.multiblockednbt.api;

import com.google.gson.JsonElement;
import net.minecraft.item.ItemStack;

public interface INBTRequirement {
    boolean satisfies(ItemStack stack);
    JsonElement serialize();
    String getName();
}
