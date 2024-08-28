package com.teamdimensional.multiblockednbt.capability.fluid;

import com.cleanroommc.multiblocked.api.capability.IO;
import com.cleanroommc.multiblocked.api.capability.MultiblockCapability;
import com.cleanroommc.multiblocked.api.capability.proxy.CapabilityProxy;
import com.cleanroommc.multiblocked.api.capability.trait.CapabilityTrait;
import com.cleanroommc.multiblocked.api.gui.widget.imp.recipe.ContentWidget;
import com.cleanroommc.multiblocked.api.recipe.Recipe;
import com.cleanroommc.multiblocked.jei.IJeiIngredientAdapter;
import com.google.gson.*;
import com.teamdimensional.multiblockednbt.api.INBTModifier;
import com.teamdimensional.multiblockednbt.api.INBTRecipeManager;
import com.teamdimensional.multiblockednbt.api.INBTRequirement;
import com.teamdimensional.multiblockednbt.capability.KnowledgeableCapabilityProxy;
import com.teamdimensional.multiblockednbt.component.NBTContentWidgetFluid;
import com.teamdimensional.multiblockednbt.component.NBTManagerRegistry;
import com.teamdimensional.multiblockednbt.component.NBTModificationRecipe;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IIngredientType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class FluidNBTMultiblockCapability extends MultiblockCapability<FluidNBTMultiblockCapability.NBTModificationRecipeFluid> {
    public static final FluidNBTMultiblockCapability INSTANCE = new FluidNBTMultiblockCapability();

    private FluidNBTMultiblockCapability() {
        super("fluid_nbt", new Color(0xFF3357FF).getRGB(), new FluidNBTJEIAdapter());
    }

    @Override
    public NBTModificationRecipeFluid defaultContent() {
        return new NBTModificationRecipeFluid(NBTManagerRegistry.FLUIDS, "_", new LinkedList<>(), new LinkedList<>());
    }

    @Override
    public boolean isBlockHasCapability(@Nonnull IO io, @Nonnull TileEntity tileEntity) {
        return !getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, tileEntity).isEmpty();
    }

    @Override
    public NBTModificationRecipeFluid copyInner(NBTModificationRecipeFluid nbtModificationRecipe) {
        return nbtModificationRecipe.copy();
    }

    @Override
    public boolean hasTrait() {
        return true;
    }

    @Override
    public CapabilityTrait createTrait() {
        return new FluidNBTCapabilityTrait();
    }

    @Override
    protected CapabilityProxy<? extends NBTModificationRecipeFluid> createProxy(@Nonnull IO io, @Nonnull TileEntity tileEntity) {
        return new FluidNBTCapabilityProxy(tileEntity);
    }

    @Override
    public NBTModificationRecipeFluid deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!(json instanceof JsonObject)) throw new JsonParseException("NBTMod recipes should be objects");
        return NBTModificationRecipeFluid.fromBase(NBTManagerRegistry.deserialize(NBTManagerRegistry.FLUIDS, (JsonObject) json));
    }

    @Override
    public JsonElement serialize(NBTModificationRecipeFluid src, Type typeOfSrc, JsonSerializationContext context) {
        return src.serialize();
    }

    @Override
    public ContentWidget<? super NBTModificationRecipeFluid> createContentWidget() {
        return new NBTContentWidgetFluid();
    }

    public static class FluidNBTCapabilityProxy extends KnowledgeableCapabilityProxy<IFluidHandler, NBTModificationRecipeFluid> {

        public FluidNBTCapabilityProxy(TileEntity tileEntity) {
            super(INSTANCE, tileEntity, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
        }

        private void writeFluidstack(String key, FluidStack stack, boolean simulate) {
            IStorageProcessCapabilityFluid capa = controller.getCapability(
                StorageProcessStorageFluid.STORAGE_PROCESS_CAPABILITY, EnumFacing.NORTH);
            assert capa != null;
            capa.setItem(key, stack, simulate);
        }

        private FluidStack readFluidstack(String key, boolean simulate) {
            IStorageProcessCapabilityFluid capa = controller.getCapability(
                StorageProcessStorageFluid.STORAGE_PROCESS_CAPABILITY, EnumFacing.NORTH);
            assert capa != null;
            return capa.getItem(key, simulate);
        }

        @Override
        protected List<NBTModificationRecipeFluid> handleRecipeInner(IO io, Recipe recipe, List<NBTModificationRecipeFluid> left,
                                                                    @Nullable String slotName, boolean simulate) {
            IFluidHandler capability = getCapability(slotName);
            if (capability == null || controller == null) return left;

            Iterator<NBTModificationRecipeFluid> iterator = left.iterator();
            if (io == IO.IN) {
                while (iterator.hasNext()) {
                    NBTModificationRecipeFluid ingredient = iterator.next();
                    IFluidTankProperties[] properties = capability.getTankProperties();
                    for (IFluidTankProperties prop : properties) {
                        FluidStack content = prop.getContents();
                        if (content != null && ingredient.canApply(content)) {
                            content.amount = ingredient.getProcessedQuantity();
                            FluidStack extracted = capability.drain(content, !simulate);
                            writeFluidstack(ingredient.getKey(), extracted, simulate);
                            getTileEntity().markDirty();
                            iterator.remove();
                            break;
                        }
                    }
                }
            } else if (io == IO.OUT) {
                if (!iterator.hasNext()) return left;
                NBTModificationRecipe<FluidStack> ingredient = iterator.next();
                FluidStack stack = readFluidstack(ingredient.getKey(), simulate);
                stack = ingredient.apply(stack);
                int filledAmount = capability.fill(stack, !simulate);
                if (filledAmount == stack.amount) {
                    iterator.remove();
                    writeFluidstack(ingredient.getKey(), new FluidStack(FluidRegistry.WATER, 0), simulate);
                } else {
                    writeFluidstack(ingredient.getKey(), stack, simulate);
                }
            }

            return left.isEmpty() ? null : left;
        }
    }

    public static class NBTModificationRecipeFluid extends NBTModificationRecipe<FluidStack> {
        public NBTModificationRecipeFluid(
            INBTRecipeManager<FluidStack> manager, String key, List<INBTModifier<FluidStack>> inbtModifiers, List<INBTRequirement<FluidStack>> inbtRequirements) {
            super(manager, key, inbtModifiers, inbtRequirements);
        }

        public static NBTModificationRecipeFluid fromBase(NBTModificationRecipe<FluidStack> recipe) {
            return new NBTModificationRecipeFluid(NBTManagerRegistry.FLUIDS, recipe.getKey(), recipe.getModifiers(), recipe.getRequirements());
        }

        @Override
        public NBTModificationRecipeFluid copy() {
            return fromBase(this);
        }

        @Override
        public boolean canApply(FluidStack stack) {
            return stack.amount >= getProcessedQuantity() && super.canApply(stack);
        }
    }

    public static class FluidNBTJEIAdapter implements IJeiIngredientAdapter<NBTModificationRecipeFluid, FluidStack> {

        @Override
        public Class<NBTModificationRecipeFluid> getInternalIngredientType() {
            return NBTModificationRecipeFluid.class;
        }

        @Override
        public IIngredientType<FluidStack> getJeiIngredientType() {
            return VanillaTypes.FLUID;
        }

        @Override
        public Stream<FluidStack> apply(NBTModificationRecipeFluid recipe) {
            return recipe.getMatchingStacks(IO.BOTH).stream().map(r -> r.stack);
        }
    }
}
