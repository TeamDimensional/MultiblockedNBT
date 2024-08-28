package com.teamdimensional.multiblockednbt.api;

import com.cleanroommc.multiblocked.api.capability.IO;
import com.teamdimensional.multiblockednbt.component.StackWithTooltip;

public interface INBTRecipeManager<T> {
    /**
     * Checks whether a certain stack is empty. This is used to short-circuit decline recipes with empty inputs.
     * @param stack The stack to check
     * @return True if the stack is empty
     */
    boolean isEmpty(T stack);

    /**
     * Makes the default item to be displayed in JEI.
     * @param io Whether the item is shown on input or on output
     * @return The default item
     */
    StackWithTooltip<T> getDefaultItem(IO io);

    /**
     * Returns the amount of items that's processed at the same time. This may be unnecessary depending on the capability implementation.
     * @return The quantity
     */
    int getProcessedQuantity();
}
