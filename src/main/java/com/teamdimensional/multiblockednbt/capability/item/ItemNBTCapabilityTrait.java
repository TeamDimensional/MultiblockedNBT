package com.teamdimensional.multiblockednbt.capability.item;

import com.cleanroommc.multiblocked.api.capability.trait.SingleCapabilityTrait;
import com.google.gson.JsonParser;
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
            public void setItem(String key, ItemStack stack, boolean simulate) {
                super.setItem(key, stack, simulate);
                markAsDirty();
            }
        };
    }

    @Override
    public void onDrops(NonNullList<ItemStack> drops, EntityPlayer player) {
        for (String key : storageProcessInventory.getKeys()) {
            ItemStack stack = storageProcessInventory.getItem(key, false);
            if (stack != null) drops.add(stack);
        }
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
