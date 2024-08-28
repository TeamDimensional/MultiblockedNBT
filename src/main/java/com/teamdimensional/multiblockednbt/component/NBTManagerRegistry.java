package com.teamdimensional.multiblockednbt.component;

import com.cleanroommc.multiblocked.api.capability.IO;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.teamdimensional.multiblockednbt.MultiblockedNBT;
import com.teamdimensional.multiblockednbt.api.INBTModifier;
import com.teamdimensional.multiblockednbt.api.INBTRecipeManager;
import com.teamdimensional.multiblockednbt.api.INBTRequirement;
import com.teamdimensional.multiblockednbt.factory.NBTModifierFactory;
import com.teamdimensional.multiblockednbt.factory.NBTRequirementFactory;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.LinkedList;
import java.util.List;

public class NBTManagerRegistry {

    public static final INBTRecipeManager<ItemStack> ITEMS = new NBTManagerItem();
    public static final INBTRecipeManager<FluidStack> FLUIDS = new NBTManagerFluid();

    @SuppressWarnings("unchecked")
    public static <T> NBTModificationRecipe<T> deserialize(INBTRecipeManager<T> manager, JsonObject element) throws JsonParseException {
        List<INBTModifier<T>> mods = new LinkedList<>();
        JsonArray modsArray = element.getAsJsonArray("modifiers");
        for (JsonElement elt : modsArray) {
            INBTModifier<T> mod = (INBTModifier<T>) NBTModifierFactory.deserialize(elt);
            if (mod == null) {
                MultiblockedNBT.LOGGER.warn("Unable to create a modifier for the element {}!", elt);
                continue;
            }
            mods.add(mod);
        }
        List<INBTRequirement<T>> reqs = new LinkedList<>();
        JsonArray reqsArray = element.getAsJsonArray("requirements");
        for (JsonElement elt : reqsArray) {
            INBTRequirement<T> req = (INBTRequirement<T>) NBTRequirementFactory.deserialize(elt);
            if (req == null) {
                MultiblockedNBT.LOGGER.warn("Unable to create a requirement for the element {}!", elt);
                continue;
            }
            reqs.add(req);
        }
        String key = element.has("key") ? element.get("key").getAsString() : "_";
        return new NBTModificationRecipe<T>(manager, key, mods, reqs);
    }

    private static class NBTManagerItem implements INBTRecipeManager<ItemStack> {

        @Override
        public boolean isEmpty(ItemStack stack) {
            return stack.isEmpty();
        }

        @Override
        public StackWithTooltip<ItemStack> getDefaultItem(IO io) {
            ItemStack stack = new ItemStack(Items.IRON_AXE).setStackDisplayName("Any item");
            return new StackWithTooltip<>(stack, io);
        }

        @Override
        public int getProcessedQuantity() {
            return 1;
        }
    }

    private static class NBTManagerFluid implements INBTRecipeManager<FluidStack> {

        @Override
        public boolean isEmpty(FluidStack stack) {
            return stack.amount == 0;
        }

        @Override
        public StackWithTooltip<FluidStack> getDefaultItem(IO io) {
            FluidStack stack = new FluidStack(FluidRegistry.WATER, 1000);
            return new StackWithTooltip<>(stack, io);
        }

        @Override
        public int getProcessedQuantity() {
            return 1000;  // 1 bucket
        }
    }

}
