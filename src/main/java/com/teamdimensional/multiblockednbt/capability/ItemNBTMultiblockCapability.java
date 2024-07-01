package com.teamdimensional.multiblockednbt.capability;

import com.cleanroommc.multiblocked.api.capability.IO;
import com.cleanroommc.multiblocked.api.capability.MultiblockCapability;
import com.cleanroommc.multiblocked.api.capability.proxy.CapabilityProxy;
import com.cleanroommc.multiblocked.api.capability.trait.CapabilityTrait;
import com.cleanroommc.multiblocked.api.recipe.Recipe;
import com.cleanroommc.multiblocked.jei.IJeiIngredientAdapter;
import com.google.gson.*;
import com.teamdimensional.multiblockednbt.MultiblockedNBT;
import com.teamdimensional.multiblockednbt.component.NBTModificationRecipe;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IIngredientType;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class ItemNBTMultiblockCapability extends MultiblockCapability<NBTModificationRecipe> {
    public static final ItemNBTMultiblockCapability INSTANCE = new ItemNBTMultiblockCapability();

    private ItemNBTMultiblockCapability() {
        super("item_nbt", new Color(0xFFD96106).getRGB(), new ItemNBTJEIAdapter());
    }

    @Override
    public NBTModificationRecipe defaultContent() {
        return new NBTModificationRecipe(new LinkedList<>(), new LinkedList<>());
    }

    @Override
    public boolean isBlockHasCapability(@Nonnull IO io, @Nonnull TileEntity tileEntity) {
        return !getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, tileEntity).isEmpty();
    }

    @Override
    public NBTModificationRecipe copyInner(NBTModificationRecipe nbtModificationRecipe) {
        return nbtModificationRecipe.copy();
    }

    @Override
    public boolean hasTrait() {
        return true;
    }

    @Override
    public CapabilityTrait createTrait() {
        return new ItemNBTCapabilityTrait();
    }

    @Override
    protected CapabilityProxy<? extends NBTModificationRecipe> createProxy(@Nonnull IO io, @Nonnull TileEntity tileEntity) {
        return new ItemNBTCapabilityProxy(tileEntity);
    }

    @Override
    public NBTModificationRecipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!(json instanceof JsonObject)) throw new JsonParseException("NBTMod recipes should be objects");
        return NBTModificationRecipe.deserialize((JsonObject) json);
    }

    @Override
    public JsonElement serialize(NBTModificationRecipe src, Type typeOfSrc, JsonSerializationContext context) {
        return src.serialize();
    }

    public static class ItemNBTCapabilityProxy extends KnowledgeableCapabilityProxy<IItemHandler, NBTModificationRecipe> {

        public ItemNBTCapabilityProxy(TileEntity tileEntity) {
            super(INSTANCE, tileEntity, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        }

        private void writeItemstack(ItemStack stack, boolean simulate) {
            IStorageProcessCapability capa = controller.getCapability(
                StorageProcessStorage.STORAGE_PROCESS_CAPABILITY, EnumFacing.NORTH);
            assert capa != null;
            capa.setItem(stack, simulate);
        }

        private ItemStack readItemstack(boolean simulate) {
            IStorageProcessCapability capa = controller.getCapability(
                StorageProcessStorage.STORAGE_PROCESS_CAPABILITY, EnumFacing.NORTH);
            assert capa != null;
            return capa.getItem(simulate);
        }

        @Override
        protected List<NBTModificationRecipe> handleRecipeInner(IO io, Recipe recipe, List<NBTModificationRecipe> left,
                                                                @Nullable String slotName, boolean simulate) {
            IItemHandler capability = getCapability(slotName);
            if (capability == null) return left;

            Iterator<NBTModificationRecipe> iterator = left.iterator();
            if (io == IO.IN) {
                while (iterator.hasNext()) {
                    NBTModificationRecipe ingredient = iterator.next();
                    for (int i = 0; i < capability.getSlots(); i++) {
                        ItemStack itemStack = capability.getStackInSlot(i);
                        if (ingredient.canApply(itemStack)) {
                            ItemStack extracted = capability.extractItem(i, 1, simulate);
                            writeItemstack(extracted, simulate);
                            getTileEntity().markDirty();
                            iterator.remove();
                            break;
                        }
                    }
                }
            } else if (io == IO.OUT) {
                ItemStack stack = readItemstack(simulate);
                if (stack != null && !stack.isEmpty()) {
                    while (iterator.hasNext()) {
                        NBTModificationRecipe ingredient = iterator.next();
                        ingredient.apply(stack);
                        for (int i = 0; i < capability.getSlots(); i++) {
                            stack = capability.insertItem(i, stack.copy(), simulate);
                            if (stack.isEmpty()) break;
                        }
                        if (stack.isEmpty()) {
                            iterator.remove();
                            writeItemstack(ItemStack.EMPTY, simulate);
                        } else {
                            writeItemstack(stack, simulate);
                        }
                    }
                }
            }

            return left.isEmpty() ? null : left;
        }
    }

    public static class ItemNBTJEIAdapter implements IJeiIngredientAdapter<NBTModificationRecipe, ItemStack> {

        @Override
        public Class<NBTModificationRecipe> getInternalIngredientType() {
            return NBTModificationRecipe.class;
        }

        @Override
        public IIngredientType<ItemStack> getJeiIngredientType() {
            return VanillaTypes.ITEM;
        }

        @Override
        public Stream<ItemStack> apply(NBTModificationRecipe recipe) {
            return Arrays.stream(recipe.getMatchingStacks());
        }
    }
}
