package com.teamdimensional.multiblockednbt.api;

import com.google.gson.JsonElement;
import com.teamdimensional.multiblockednbt.component.StackWithTooltip;

/**
 * NBT Requirement type. Input items in the NBT recipes will be checked against this requirement if enabled in the recipe map.
 * @param <T> ItemStack or FluidStack
 */
public interface INBTRequirement<T> {
    /**
     * Checks whether an item satisfies this requirement. If false is returned, the recipe gets ignored by the multiblock.
     * @param stack The item stack/fluid stack being processed
     * @return Whether the requirement is satisfied
     */
    boolean satisfies(T stack);

    /**
     * Turns the internal data of modifier into JSON.
     * @return JSONElement after serialization
     */
    JsonElement serialize();

    /**
     * Returns the modifier name, which is a string that determines this modifier in the modifier registry.
     * @return The modifier name
     */
    String getName();

    /**
     * Modifies the stack info as displayed in JEI. Most modifiers should add tooltips to this.
     * All modifiers should override this, even though it's technically optional.
     * @param stack The stack & tooltip to modify
     */
    default void modifyStack(StackWithTooltip<T> stack) {}
}
