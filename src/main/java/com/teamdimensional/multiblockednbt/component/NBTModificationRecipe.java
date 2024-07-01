package com.teamdimensional.multiblockednbt.component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.teamdimensional.multiblockednbt.MultiblockedNBT;
import com.teamdimensional.multiblockednbt.api.INBTModifier;
import com.teamdimensional.multiblockednbt.api.INBTRequirement;
import com.teamdimensional.multiblockednbt.factory.NBTModifierFactory;
import com.teamdimensional.multiblockednbt.factory.NBTRequirementFactory;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.LinkedList;
import java.util.List;

public class NBTModificationRecipe {
    private final INBTModifier[] modifiers;
    private final INBTRequirement[] requirements;

    public NBTModificationRecipe(INBTModifier[] modifiers, INBTRequirement[] requirements) {
        this.modifiers = modifiers;
        this.requirements = requirements;
    }

    public NBTModificationRecipe(List<INBTModifier> modifiers, List<INBTRequirement> requirements) {
        this(modifiers.toArray(new INBTModifier[0]), requirements.toArray(new INBTRequirement[0]));
    }

    public NBTModificationRecipe copy() {
        return new NBTModificationRecipe(modifiers, requirements);
    }

    public boolean canApply(ItemStack stack) {
        if (stack.isEmpty()) return false;
        for (INBTRequirement req : requirements) {
            if (!req.satisfies(stack)) return false;
        }
        for (INBTModifier mod : modifiers) {
            if (!mod.canApply(stack)) return false;
        }
        return true;
    }

    public void apply(ItemStack stack) {
        for (INBTModifier mod : modifiers) {
            mod.applyTo(stack);
        }
    }

    public JsonObject serialize() {
        JsonObject obj = new JsonObject();
        JsonArray modifiers = new JsonArray();
        for (INBTModifier mod : this.modifiers) modifiers.add(NBTModifierFactory.serialize(mod));
        obj.add("modifiers", modifiers);
        JsonArray requirements = new JsonArray();
        for (INBTRequirement req : this.requirements) requirements.add(NBTRequirementFactory.serialize(req));
        obj.add("requirements", requirements);
        return obj;
    }

    public static NBTModificationRecipe deserialize(JsonObject element) throws JsonParseException {
        List<INBTModifier> mods = new LinkedList<>();
        JsonArray modsArray = element.getAsJsonArray("modifiers");
        for (JsonElement elt : modsArray) {
            INBTModifier mod = NBTModifierFactory.deserialize(elt);
            if (mod == null) {
                MultiblockedNBT.LOGGER.warn("Unable to create a modifier for the element {}!", elt);
                continue;
            }
            mods.add(mod);
        }
        List<INBTRequirement> reqs = new LinkedList<>();
        JsonArray reqsArray = element.getAsJsonArray("requirements");
        for (JsonElement elt : reqsArray) {
            INBTRequirement req = NBTRequirementFactory.deserialize(elt);
            if (req == null) {
                MultiblockedNBT.LOGGER.warn("Unable to create a modifier for the element {}!", elt);
                continue;
            }
            reqs.add(req);
        }
        return new NBTModificationRecipe(mods, reqs);
    }

    public ItemStack[] getMatchingStacks() {
        // placeholder
        return new ItemStack[]{new ItemStack(Items.IRON_AXE)};
    }
}
