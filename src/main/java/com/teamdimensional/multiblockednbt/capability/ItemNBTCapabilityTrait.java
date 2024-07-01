package com.teamdimensional.multiblockednbt.capability;

import com.cleanroommc.multiblocked.Multiblocked;
import com.cleanroommc.multiblocked.api.capability.trait.SingleCapabilityTrait;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.teamdimensional.multiblockednbt.MultiblockedNBT;
import com.teamdimensional.multiblockednbt.component.NBTModificationRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemNBTCapabilityTrait extends SingleCapabilityTrait {
    private @Nullable NBTModificationRecipe executingRecipe = null;
    private @Nullable ItemStack processedStack = null;
    private final IStorageProcessCapability storageProcessInventory;

    public ItemNBTCapabilityTrait() {
        super(ItemNBTMultiblockCapability.INSTANCE);
        storageProcessInventory = new StorageProcessImplementation() {
            @Override
            public void setItem(ItemStack stack, boolean simulate) {
                super.setItem(stack, simulate);
                markAsDirty();
            }
        };
    }

    @Override
    public void serialize(@Nullable JsonElement jsonElement) {
        super.serialize(jsonElement);
        if (!(jsonElement instanceof JsonObject)) jsonElement = new JsonObject();
        JsonObject jsonObject = (JsonObject) jsonElement;
        JsonElement recipe = jsonObject.get("recipe");
        if (recipe != null) executingRecipe = NBTModificationRecipe.deserialize((JsonObject) recipe);
        JsonElement stack = jsonObject.get("stack");
        if (stack != null) processedStack = Multiblocked.GSON.fromJson(stack.getAsString(), ItemStack.class);
        if ((executingRecipe == null || processedStack == null) && (executingRecipe != null || processedStack != null)) {
            MultiblockedNBT.LOGGER.warn("Error while deserializing trait: one of (stack,recipe) was null but other wasn't");
            executingRecipe = null;
            processedStack = null;
        }
    }

    @Override
    public JsonElement deserialize() {
        JsonObject obj = super.deserialize().getAsJsonObject();
        if (executingRecipe != null) obj.add("recipe", executingRecipe.serialize());
        if (processedStack != null) obj.addProperty("stack", Multiblocked.GSON.toJson(processedStack));
        return obj;
    }

    @Override
    public void onDrops(NonNullList<ItemStack> drops, EntityPlayer player) {
        if (processedStack != null) drops.add(processedStack);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        String str = compound.getString("d");
        serialize(new JsonParser().parse(str));
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setString("d", deserialize().toString());
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability != StorageProcessStorage.STORAGE_PROCESS_CAPABILITY) return null;
        return (T) storageProcessInventory;
    }

    @Override
    public boolean hasUpdate() {
        return true;
    }
}
