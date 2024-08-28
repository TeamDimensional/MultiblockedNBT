package com.teamdimensional.multiblockednbt.modifier;

import com.cleanroommc.multiblocked.api.capability.IO;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.teamdimensional.multiblockednbt.api.INBTModifier;
import com.teamdimensional.multiblockednbt.component.StackWithTooltip;
import mcjty.deepresonance.fluid.DRFluidRegistry;
import mcjty.deepresonance.fluid.LiquidCrystalFluidTagData;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fluids.FluidStack;

import java.util.LinkedList;
import java.util.List;

public class NBTModifierRCLQuality implements INBTModifier<FluidStack> {
    private final ImmutableList<Float> deltas;
    private final ImmutableList<Float> caps;

    private final float deltaQuality;
    private final float deltaPurity;
    private final float deltaStrength;
    private final float deltaEfficiency;

    private final float maxQuality;
    private final float maxPurity;
    private final float maxStrength;
    private final float maxEfficiency;

    public NBTModifierRCLQuality(List<Float> deltas, List<Float> caps) {
        this.deltas = ImmutableList.copyOf(deltas);
        this.caps = ImmutableList.copyOf(caps);

        deltaQuality = deltas.get(0);
        deltaPurity = deltas.get(1);
        deltaStrength = deltas.get(2);
        deltaEfficiency = deltas.get(3);

        maxQuality = caps.get(0);
        maxPurity = caps.get(1);
        maxStrength = caps.get(2);
        maxEfficiency = caps.get(3);
    }

    @Override
    public boolean canApply(FluidStack stack) {
        LiquidCrystalFluidTagData data = LiquidCrystalFluidTagData.fromStack(stack);
        if (data == null) return false;
        if (data.getQuality() + deltaQuality < 0) return false;
        if (data.getPurity() + deltaPurity < 0) return false;
        if (data.getStrength() + deltaStrength < 0) return false;
        if (data.getEfficiency() + deltaEfficiency < 0) return false;

        return data.getQuality() <= maxQuality || data.getPurity() <= maxPurity ||
               data.getStrength() <= maxStrength || data.getEfficiency() <= maxEfficiency;
    }

    @Override
    public FluidStack applyTo(FluidStack stack) {
        LiquidCrystalFluidTagData data = LiquidCrystalFluidTagData.fromStack(stack);
        data.setQuality(Math.min(data.getQuality() + deltaQuality, maxQuality));
        data.setPurity(Math.min(data.getPurity() + deltaPurity, maxPurity));
        data.setStrength(Math.min(data.getStrength() + deltaStrength, maxStrength));
        data.setEfficiency(Math.min(data.getEfficiency() + deltaEfficiency, maxEfficiency));
        data.save();
        return stack;
    }

    @Override
    public JsonElement serialize() {
        JsonObject output = new JsonObject();
        JsonArray deltas = new JsonArray();
        for (float d : this.deltas) deltas.add(d);
        JsonArray caps = new JsonArray();
        for (float c : this.caps) caps.add(c);

        output.add("deltas", deltas);
        output.add("caps", caps);
        return output;
    }

    public static NBTModifierRCLQuality deserialize(JsonElement o) {
        if (!(o instanceof JsonObject)) return null;

        JsonArray deltas = ((JsonObject) o).get("deltas").getAsJsonArray();
        JsonArray caps = ((JsonObject) o).get("caps").getAsJsonArray();
        List<Float> deltasArr = new LinkedList<>(), capsArr = new LinkedList<>();
        for (JsonElement d : deltas) deltasArr.add(d.getAsFloat());
        for (JsonElement d : caps) capsArr.add(d.getAsFloat());
        if (deltasArr.size() != 4 || capsArr.size() != 4) throw new JsonParseException("Invalid size of RCLQuality modifier arrays");

        return new NBTModifierRCLQuality(deltasArr, capsArr);
    }

    @Override
    public String getName() {
        return "deepresonance:rcl_quality";
    }

    private void addInTooltip(String key, float value, StackWithTooltip<FluidStack> pair) {
        if (value < 0) {
            pair.addTooltip(I18n.format("multiblockednbt.deepresonance.modifier.minimum." + key, -value));
        }
    }

    private void addOutTooltip(String key, float value, float maxValue, StackWithTooltip<FluidStack> pair) {
        if (value > 0) {
            pair.addTooltip(I18n.format("multiblockednbt.deepresonance.modifier.adds." + key, value, maxValue));
        }
    }

    @Override
    public void modifyStack(StackWithTooltip<FluidStack> pair) {
        pair.stack = new FluidStack(DRFluidRegistry.liquidCrystal, pair.stack.amount);
        if (pair.io == IO.IN) {
            addInTooltip("quality", deltaQuality, pair);
            addInTooltip("purity", deltaPurity, pair);
            addInTooltip("strength", deltaStrength, pair);
            addInTooltip("efficiency", deltaEfficiency, pair);
        } else {
            addOutTooltip("quality", deltaQuality, maxQuality, pair);
            addOutTooltip("purity", deltaPurity, maxPurity, pair);
            addOutTooltip("strength", deltaStrength, maxStrength, pair);
            addOutTooltip("efficiency", deltaEfficiency, maxEfficiency, pair);
        }
    }
}
