package com.teamdimensional.multiblockednbt.component;

import com.cleanroommc.multiblocked.Multiblocked;
import com.cleanroommc.multiblocked.api.gui.widget.imp.TankWidget;
import com.cleanroommc.multiblocked.api.gui.widget.imp.recipe.ContentWidget;
import com.teamdimensional.multiblockednbt.api.INBTRequirement;
import com.teamdimensional.multiblockednbt.requirement.NBTRequirementFluid;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class NBTContentWidgetFluid extends ContentWidget<NBTModificationRecipe<FluidStack>> {
    protected FluidTank handler;

    @Override
    protected void onContentUpdate() {
        StackWithTooltip<FluidStack> pair = getFluidData(content);
        if (Multiblocked.isClient()) {
            List<String> tooltips = new ArrayList<>();
            tooltips.add(pair.stack.getFluid().getLocalizedName(pair.stack));
            tooltips.add(I18n.format("multiblocked.fluid.amount", pair.stack.amount, pair.stack.amount));
            tooltips.addAll(pair.tooltip);
            setHoverTooltip(tooltips.stream().reduce((a, b) -> a + "\n" + b).orElse("fluid"));
        }

        if (handler != null) {
            handler.drainInternal(Integer.MAX_VALUE, true);
            handler.setCapacity(pair.stack.amount);
            handler.fillInternal(pair.stack.copy(), true);
        } else {
            addWidget(new TankWidget(new FluidTank(pair.stack.copy(), content.getProcessedQuantity()), 1, 1, false, false).setDrawHoverTips(false));
        }
    }

    @Override
    public NBTModificationRecipe<FluidStack> getJEIContent(Object content) {
        if (content instanceof FluidStack) {
            INBTRequirement<FluidStack> requirement = new NBTRequirementFluid((FluidStack) content);
            List<INBTRequirement<FluidStack>> reqs = new LinkedList<>();
            reqs.add(requirement);
            return new NBTModificationRecipe<>(NBTManagerRegistry.FLUIDS, new LinkedList<>(), reqs);
        }
        return null;
    }

    @Nullable
    @Override
    public Object getJEIIngredient(NBTModificationRecipe<FluidStack> content) {
        return content.getMatchingStacks(io).get(0).stack;
    }

    public StackWithTooltip<FluidStack> getFluidData(NBTModificationRecipe<FluidStack> content) {
        // placeholder
        return content.getMatchingStacks(io).get(0);
    }
}
