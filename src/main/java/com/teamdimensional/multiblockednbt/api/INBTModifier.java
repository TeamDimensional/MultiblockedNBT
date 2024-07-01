package com.teamdimensional.multiblockednbt.api;

import com.google.gson.JsonElement;
import net.minecraft.item.ItemStack;

public interface INBTModifier {
    boolean canApply(ItemStack stack);
    void applyTo(ItemStack stack);
    JsonElement serialize();
    String getName();
}
