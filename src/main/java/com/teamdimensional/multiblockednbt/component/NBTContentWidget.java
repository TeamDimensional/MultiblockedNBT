package com.teamdimensional.multiblockednbt.component;

import com.cleanroommc.multiblocked.api.capability.IO;
import com.cleanroommc.multiblocked.api.gui.widget.imp.SlotWidget;
import com.cleanroommc.multiblocked.api.gui.widget.imp.recipe.ContentWidget;
import com.teamdimensional.multiblockednbt.api.INBTModifier;
import com.teamdimensional.multiblockednbt.api.INBTRequirement;
import com.teamdimensional.multiblockednbt.requirement.NBTRequirementItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class NBTContentWidget extends ContentWidget<NBTModificationRecipe> {
    protected ItemStackHandler handler;

    @Override
    protected void onContentUpdate() {
        if (handler == null) handler = new ItemStackHandler(1);
        StackWithTooltip pair = getItemData(content);
        handler.setStackInSlot(0, pair.stack);
        addWidget(new SlotWidget(handler, 0, 1, 1, false, false).setDrawOverlay(false).setOnAddedTooltips((s, l) -> l.addAll(pair.tooltip)));
    }

    @Override
    public NBTModificationRecipe getJEIContent(Object content) {
        if (content instanceof ItemStack) {
            INBTRequirement requirement = new NBTRequirementItem((ItemStack) content);
            return new NBTModificationRecipe(new INBTModifier[0], new INBTRequirement[]{requirement});
        }
        return null;
    }

    @Nullable
    @Override
    public Object getJEIIngredient(NBTModificationRecipe content) {
        return content.getMatchingStacks(io)[0].stack;
    }

    public StackWithTooltip getItemData(NBTModificationRecipe content) {
        // placeholder
        return content.getMatchingStacks(io)[0];
    }
}
