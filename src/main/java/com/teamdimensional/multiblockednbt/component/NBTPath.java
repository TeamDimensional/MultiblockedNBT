package com.teamdimensional.multiblockednbt.component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import javax.annotation.Nullable;

public class NBTPath {
    private final String[] parts;

    public NBTPath(@Nullable String path) {
        if (path == null || path.isEmpty()) {
            parts = new String[0];
        } else {
            parts = path.split("\\.");
        }
    }

    public NBTPath(String[] path) {
        parts = path;
    }

    public boolean isEmpty() {
        return parts.length == 0;
    }

    public @Nullable NBTBase extract(ItemStack stack) {
        return extract(stack.getTagCompound());
    }

    public @Nullable NBTBase extract(NBTTagCompound compound) {
        NBTBase tag = compound;
        for (String p : parts) {
            if (tag == null) return null;
            if (tag instanceof NBTTagCompound) {
                tag = ((NBTTagCompound) tag).getTag(p);
            } else if (tag instanceof NBTTagList) {
                try {
                    tag = ((NBTTagList) tag).get(new Integer(p));
                } catch (NumberFormatException e) {
                    return null;
                }
            } else {
                return null;
            }
        }
        return tag;
    }

    public JsonElement serialize() {
        JsonArray arr = new JsonArray();
        for (String s : parts) arr.add(s);
        return arr;
    }

}
