package com.teamdimensional.multiblockednbt.api;

import com.google.gson.JsonElement;
import com.teamdimensional.multiblockednbt.component.StackWithTooltip;

/**
 * NBT Modifier type. Input items in the NBT recipes will be modified according to the modifiers defined in the recipe map.
 * IMPORTANT NOTE: if the item is being 'modified', the modifier should be the same in Input and Output.
 * @param <T> ItemStack or FluidStack
 */
public interface INBTModifier<T> {
    /**
     * Checks whether the modifier can apply to this item. If false is returned, the recipe gets ignored by the multiblock.
     * @param stack The item stack/fluid stack being processed
     * @return Whether the modifier can be applied
     */
    boolean canApply(T stack);

    /**
     * Applies the modifier to the stack.
     * @param stack The item stack/fluid stack being processed
     * @return The same or different stack after the modifier is applied
     */
    T applyTo(T stack);

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
