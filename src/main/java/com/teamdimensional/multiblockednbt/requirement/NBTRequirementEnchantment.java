package com.teamdimensional.multiblockednbt.requirement;

import com.cleanroommc.multiblocked.api.capability.IO;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teamdimensional.multiblockednbt.api.INBTRequirement;
import com.teamdimensional.multiblockednbt.component.StackWithTooltip;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTRequirementEnchantment implements INBTRequirement<ItemStack> {
    private final String enchantment;
    private final int targetLevel;
    private final short enchantmentId;

    public NBTRequirementEnchantment(String enchantName, int level) {
        enchantment = enchantName;
        targetLevel = level;

        Enchantment ench = Enchantment.getEnchantmentByLocation(enchantName);
        assert ench != null;
        enchantmentId = (short) Enchantment.getEnchantmentID(ench);
    }

    @Override
    public boolean satisfies(ItemStack stack) {
        NBTTagList c = stack.getEnchantmentTagList();
        for (NBTBase b : c) {
            if (!(b instanceof NBTTagCompound)) continue;
            NBTTagCompound bc = (NBTTagCompound) b;
            if (bc.getShort("id") == enchantmentId && bc.getShort("lvl") >= targetLevel) {
                return true;
            }
        }

        return false;
    }

    @Override
    public JsonElement serialize() {
        JsonObject output = new JsonObject();
        output.addProperty("enchantment", enchantment);
        output.addProperty("level", targetLevel);
        return output;
    }

    public static NBTRequirementEnchantment deserialize(JsonElement o) {
        if (!(o instanceof JsonObject)) return null;
        String enchantment = ((JsonObject) o).get("enchantment").getAsString();
        int level = ((JsonObject) o).get("level").getAsInt();
        return new NBTRequirementEnchantment(enchantment, level);
    }

    @Override
    public String getName() {
        return "minecraft:enchantment";
    }

    @Override
    public void modifyStack(StackWithTooltip<ItemStack> pair) {
        if (pair.io != IO.OUT) {
            Enchantment ench = Enchantment.getEnchantmentByLocation(enchantment);
            assert ench != null;
            String requiresEnch = I18n.format("multiblockednbt.requirement.enchantment", ench.getTranslatedName(targetLevel));
            pair.addTooltip(requiresEnch);
        }
    }
}
