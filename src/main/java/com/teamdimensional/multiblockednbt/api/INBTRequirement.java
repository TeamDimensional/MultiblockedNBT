package com.teamdimensional.multiblockednbt.api;

import com.google.gson.JsonElement;
import com.teamdimensional.multiblockednbt.component.StackWithTooltip;

public interface INBTRequirement<T> {
    boolean satisfies(T stack);
    JsonElement serialize();
    String getName();

    default void modifyStack(StackWithTooltip<T> stack) {}
}
