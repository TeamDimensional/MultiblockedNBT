package com.teamdimensional.multiblockednbt.requirement;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teamdimensional.multiblockednbt.api.INBTRequirement;
import com.teamdimensional.multiblockednbt.component.StackWithTooltip;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.Objects;

public class NBTRequirementFluid implements INBTRequirement<FluidStack> {
    private final Fluid fluid;

    public NBTRequirementFluid(Fluid fluid0) {
        fluid = fluid0;
    }

    public NBTRequirementFluid(FluidStack stack) {
        this(stack.getFluid());
    }

    @Override
    public boolean satisfies(FluidStack stack) {
        return Objects.equals(stack.getFluid(), fluid);
    }

    @Override
    public JsonElement serialize() {
        JsonObject output = new JsonObject();
        output.addProperty("fluid", Objects.requireNonNull(fluid.getName()));
        return output;
    }

    public static NBTRequirementFluid deserialize(JsonElement o) {
        if (!(o instanceof JsonObject)) return null;
        String registryName = ((JsonObject) o).get("fluid").getAsString();
        return new NBTRequirementFluid(FluidRegistry.getFluid(registryName));
    }

    @Override
    public String getName() {
        return "minecraft:fluid_id";
    }

    @Override
    public void modifyStack(StackWithTooltip<FluidStack> stack) {
        stack.stack = new FluidStack(fluid, stack.stack.amount);
    }
}
