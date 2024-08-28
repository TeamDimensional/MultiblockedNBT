package com.teamdimensional.multiblockednbt.modifier;

import com.cleanroommc.multiblocked.api.capability.IO;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teamdimensional.multiblockednbt.api.INBTModifier;
import com.teamdimensional.multiblockednbt.component.StackWithTooltip;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTModifierEnchantment implements INBTModifier<ItemStack> {
    private final String enchantment;
    private final int targetLevel;
    private final short enchantmentId;

    public NBTModifierEnchantment(String enchantName, int level) {
        enchantment = enchantName;
        targetLevel = level;

        Enchantment ench = Enchantment.getEnchantmentByLocation(enchantName);
        assert ench != null;
        enchantmentId = (short) Enchantment.getEnchantmentID(ench);
    }

    @Override
    public boolean canApply(ItemStack stack) {
        if (!stack.getItem().isEnchantable(stack)) {
            return false;
        }
        Enchantment ench = Enchantment.getEnchantmentByLocation(enchantment);
        if (ench == null || !ench.canApply(stack)) {
            return false;
        }
        NBTTagList c = stack.getEnchantmentTagList();
        for (NBTBase b : c) {
            if (!(b instanceof NBTTagCompound)) continue;
            NBTTagCompound bc = (NBTTagCompound) b;
            if (bc.getShort("id") == enchantmentId && bc.getShort("lvl") >= targetLevel) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack applyTo(ItemStack stack) {
        Enchantment ench = Enchantment.getEnchantmentByLocation(enchantment);
        assert ench != null;
        // manual code here for reasons
        NBTTagList c = stack.getEnchantmentTagList();
        boolean upgraded = false;
        for (NBTBase b : c) {
            if (!(b instanceof NBTTagCompound)) continue;
            NBTTagCompound bc = (NBTTagCompound) b;
            if (bc.getShort("id") == enchantmentId) {
                bc.setShort("lvl", (short) targetLevel);
                upgraded = true;
                break;
            }
        }
        if (!upgraded) stack.addEnchantment(ench, targetLevel);
        return stack;
    }

    @Override
    public JsonElement serialize() {
        JsonObject output = new JsonObject();
        output.addProperty("enchantment", enchantment);
        output.addProperty("level", targetLevel);
        return output;
    }

    public static NBTModifierEnchantment deserialize(JsonElement o) {
        if (!(o instanceof JsonObject)) return null;
        String enchantment = ((JsonObject) o).get("enchantment").getAsString();
        int level = ((JsonObject) o).get("level").getAsInt();
        return new NBTModifierEnchantment(enchantment, level);
    }

    @Override
    public String getName() {
        return "minecraft:enchantment";
    }

    @Override
    public void modifyStack(StackWithTooltip<ItemStack> pair) {
        if (pair.io != IO.IN) {
            Enchantment ench = Enchantment.getEnchantmentByLocation(enchantment);
            assert ench != null;
            String modifiesEnch = I18n.format("multiblockednbt.modifier.enchantment", ench.getTranslatedName(targetLevel));
            pair.addTooltip(modifiesEnch);
        }
    }
}
