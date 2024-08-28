package com.teamdimensional.multiblockednbt.component;

import com.cleanroommc.multiblocked.api.capability.IO;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.teamdimensional.multiblockednbt.api.INBTModifier;
import com.teamdimensional.multiblockednbt.api.INBTRecipeManager;
import com.teamdimensional.multiblockednbt.api.INBTRequirement;
import com.teamdimensional.multiblockednbt.factory.NBTModifierFactory;
import com.teamdimensional.multiblockednbt.factory.NBTRequirementFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NBTModificationRecipe<T> {
    protected final List<INBTModifier<T>> modifiers;
    protected final List<INBTRequirement<T>> requirements;
    protected final INBTRecipeManager<T> manager;

    public NBTModificationRecipe(INBTRecipeManager<T> manager, INBTModifier<T>[] modifiers, INBTRequirement<T>[] requirements) {
        this(manager, Arrays.stream(modifiers).collect(Collectors.toList()), Arrays.stream(requirements).collect(Collectors.toList()));
    }

    public NBTModificationRecipe(INBTRecipeManager<T> manager, List<INBTModifier<T>> modifiers, List<INBTRequirement<T>> requirements) {
        this.manager = manager;
        this.modifiers = modifiers;
        this.requirements = requirements;
    }

    public NBTModificationRecipe<T> copy() {
        return new NBTModificationRecipe<>(manager, modifiers, requirements);
    }

    public List<INBTModifier<T>> getModifiers() {
        return modifiers;
    }

    public List<INBTRequirement<T>> getRequirements() {
        return requirements;
    }

    public boolean canApply(T stack) {
        if (manager.isEmpty(stack)) return false;
        for (INBTRequirement<T> req : requirements) {
            if (!req.satisfies(stack)) return false;
        }
        for (INBTModifier<T> mod : modifiers) {
            if (!mod.canApply(stack)) return false;
        }
        return true;
    }

    public int getProcessedQuantity() {
        // Temp
        return this.manager.getProcessedQuantity();
    }

    public T apply(T stack) {
        for (INBTModifier<T> mod : modifiers) {
            stack = mod.applyTo(stack);
        }
        return stack;
    }

    public JsonObject serialize() {
        JsonObject obj = new JsonObject();
        JsonArray modifiers = new JsonArray();
        for (INBTModifier<T> mod : this.modifiers) modifiers.add(NBTModifierFactory.serialize(mod));
        obj.add("modifiers", modifiers);
        JsonArray requirements = new JsonArray();
        for (INBTRequirement<T> req : this.requirements) requirements.add(NBTRequirementFactory.serialize(req));
        obj.add("requirements", requirements);
        return obj;
    }

    public List<StackWithTooltip<T>> getMatchingStacks(IO io) {
        StackWithTooltip<T> pair = manager.getDefaultItem(io);
        for (INBTRequirement<T> mod : requirements) mod.modifyStack(pair);
        for (INBTModifier<T> mod : modifiers) mod.modifyStack(pair);
        return ImmutableList.of(pair);
    }
}
