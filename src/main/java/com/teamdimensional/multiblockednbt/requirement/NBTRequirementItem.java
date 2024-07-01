package com.teamdimensional.multiblockednbt.requirement;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teamdimensional.multiblockednbt.api.INBTRequirement;
import com.teamdimensional.multiblockednbt.component.StackWithTooltip;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Objects;

public class NBTRequirementItem implements INBTRequirement {
    private final int metadata;
    private final Item item;

    public NBTRequirementItem(Item item0, int metadata0) {
        item = item0;
        metadata = metadata0;
    }

    public NBTRequirementItem(ItemStack stack) {
        this(stack.getItem(), stack.getMetadata());
    }

    @Override
    public boolean satisfies(ItemStack stack) {
        return stack.getItem().equals(item) && stack.getMetadata() == metadata;
    }

    @Override
    public JsonElement serialize() {
        JsonObject output = new JsonObject();
        output.addProperty("item", Objects.requireNonNull(item.getRegistryName()).toString());
        output.addProperty("meta", metadata);
        return output;
    }

    public static NBTRequirementItem deserialize(JsonElement o) {
        if (!(o instanceof JsonObject)) return null;
        String registryName = ((JsonObject) o).get("item").getAsString();
        int meta = ((JsonObject) o).get("meta").getAsInt();
        return new NBTRequirementItem(Item.getByNameOrId(registryName), meta);
    }

    @Override
    public String getName() {
        return "id";
    }

    @Override
    public void modifyStack(StackWithTooltip pair) {
        pair.stack = new ItemStack(item, 1, metadata);
    }
}
