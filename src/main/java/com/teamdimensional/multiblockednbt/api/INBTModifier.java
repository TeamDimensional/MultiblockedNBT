package com.teamdimensional.multiblockednbt.api;

import com.google.gson.JsonElement;
import com.teamdimensional.multiblockednbt.component.StackWithTooltip;

public interface INBTModifier<T> {
    boolean canApply(T stack);
    T applyTo(T stack);
    JsonElement serialize();
    String getName();

    default void modifyStack(StackWithTooltip<T> stack) {}
}
