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
    public void onDrops(NonNullList<ItemStack> drops, EntityPlayer player) {
        ItemStack stack = storageProcessInventory.getItem(false);
        if (stack != null) drops.add(stack);
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
