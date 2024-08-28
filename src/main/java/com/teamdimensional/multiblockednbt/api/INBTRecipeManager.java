package com.teamdimensional.multiblockednbt.api;

import com.cleanroommc.multiblocked.api.capability.IO;
import com.teamdimensional.multiblockednbt.component.StackWithTooltip;

public interface INBTRecipeManager<T> {
    boolean isEmpty(T stack);
    StackWithTooltip<T> getDefaultItem(IO io);
    int getProcessedQuantity();
}
