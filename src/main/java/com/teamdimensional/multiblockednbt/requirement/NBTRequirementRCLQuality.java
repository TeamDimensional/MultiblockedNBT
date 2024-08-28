package com.teamdimensional.multiblockednbt.requirement;

import com.cleanroommc.multiblocked.api.capability.IO;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.teamdimensional.multiblockednbt.api.INBTRequirement;
import com.teamdimensional.multiblockednbt.component.StackWithTooltip;
import mcjty.deepresonance.fluid.DRFluidRegistry;
import mcjty.deepresonance.fluid.LiquidCrystalFluidTagData;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fluids.FluidStack;

import java.util.LinkedList;
import java.util.List;

public class NBTRequirementRCLQuality implements INBTRequirement<FluidStack> {
    private final ImmutableList<Float> mins;

    private final float minQuality;
    private final float minPurity;
    private final float minStrength;
    private final float minEfficiency;

    public NBTRequirementRCLQuality(List<Float> mins) {
        this.mins = ImmutableList.copyOf(mins);

        minQuality = mins.get(0);
        minPurity = mins.get(1);
        minStrength = mins.get(2);
        minEfficiency = mins.get(3);
    }

    @Override
    public boolean satisfies(FluidStack stack) {
        LiquidCrystalFluidTagData data = LiquidCrystalFluidTagData.fromStack(stack);
        if (data == null) return false;

        return data.getQuality() >= minQuality && data.getPurity() >= minPurity &&
            data.getStrength() >= minStrength && data.getEfficiency() >= minEfficiency;
    }

    @Override
    public JsonElement serialize() {
        JsonObject output = new JsonObject();
        JsonArray mins = new JsonArray();
        for (float c : this.mins) mins.add(c);

        output.add("mins", mins);
        return output;
    }

    public static NBTRequirementRCLQuality deserialize(JsonElement o) {
        if (!(o instanceof JsonObject)) return null;

        JsonArray mins = ((JsonObject) o).get("mins").getAsJsonArray();
        List<Float> minsArr = new LinkedList<>();
        for (JsonElement d : mins) minsArr.add(d.getAsFloat());
        if (minsArr.size() != 4) throw new JsonParseException("Invalid size of RCLQuality requirement array");

        return new NBTRequirementRCLQuality(minsArr);
    }

    @Override
    public String getName() {
        return "deepresonance:rcl_quality";
    }

    private void addInTooltip(String key, float value, StackWithTooltip<FluidStack> pair) {
        if (value > 0) {
            pair.addTooltip(I18n.format("multiblockednbt.deepresonance.requirement." + key, value));
        }
    }

    @Override
    public void modifyStack(StackWithTooltip<FluidStack> pair) {
        pair.stack = new FluidStack(DRFluidRegistry.liquidCrystal, pair.stack.amount);
        if (pair.io == IO.IN) {
            addInTooltip("quality", minQuality, pair);
            addInTooltip("purity", minPurity, pair);
            addInTooltip("strength", minStrength, pair);
            addInTooltip("efficiency", minEfficiency, pair);
        }
    }
}
