package com.teamdimensional.multiblockednbt.component;

import com.cleanroommc.multiblocked.api.gui.widget.imp.SlotWidget;
import com.cleanroommc.multiblocked.api.gui.widget.imp.recipe.ContentWidget;
import com.teamdimensional.multiblockednbt.api.INBTRequirement;
import com.teamdimensional.multiblockednbt.requirement.NBTRequirementItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public class NBTContentWidget extends ContentWidget<NBTModificationRecipe<ItemStack>> {
    protected ItemStackHandler handler;

    @Override
    protected void onContentUpdate() {
        if (handler == null) handler = new ItemStackHandler(1);
        StackWithTooltip<ItemStack> pair = getItemData(content);
        handler.setStackInSlot(0, pair.stack);
        addWidget(new SlotWidget(handler, 0, 1, 1, false, false).setDrawOverlay(false).setOnAddedTooltips((s, l) -> l.addAll(pair.tooltip)));
    }

    @Override
    public NBTModificationRecipe<ItemStack> getJEIContent(Object content) {
        if (content instanceof ItemStack) {
            INBTRequirement<ItemStack> requirement = new NBTRequirementItem((ItemStack) content);
            List<INBTRequirement<ItemStack>> reqs = new LinkedList<>();
            reqs.add(requirement);
            return new NBTModificationRecipe<>(NBTManagerRegistry.ITEMS, "_", new LinkedList<>(), reqs);
        }
        return null;
    }

    @Nullable
    @Override
    public Object getJEIIngredient(NBTModificationRecipe<ItemStack> content) {
        return content.getMatchingStacks(io).get(0).stack;
    }

    public StackWithTooltip<ItemStack> getItemData(NBTModificationRecipe<ItemStack> content) {
        // placeholder
        return content.getMatchingStacks(io).get(0);
    }
}
