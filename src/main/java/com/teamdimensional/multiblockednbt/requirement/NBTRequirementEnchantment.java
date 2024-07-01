package com.teamdimensional.multiblockednbt.requirement;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teamdimensional.multiblockednbt.MultiblockedNBT;
import com.teamdimensional.multiblockednbt.api.INBTRequirement;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTRequirementEnchantment implements INBTRequirement {
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
        return "ench";
    }
}
