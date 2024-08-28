package com.teamdimensional.multiblockednbt;

import com.cleanroommc.multiblocked.api.registry.MbdCapabilities;
import com.teamdimensional.multiblockednbt.capability.item.ItemNBTMultiblockCapability;
import com.teamdimensional.multiblockednbt.capability.item.StorageProcessImplementation;
import com.teamdimensional.multiblockednbt.factory.NBTModifierFactory;
import com.teamdimensional.multiblockednbt.factory.NBTRequirementFactory;
import com.teamdimensional.multiblockednbt.modifier.NBTModifierEnchantment;
import com.teamdimensional.multiblockednbt.requirement.NBTRequirementEnchantment;
import com.teamdimensional.multiblockednbt.requirement.NBTRequirementFluid;
import com.teamdimensional.multiblockednbt.requirement.NBTRequirementItem;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION, dependencies = "required-after:multiblocked")
public class MultiblockedNBT {

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);

    /**
     * <a href="https://cleanroommc.com/wiki/forge-mod-development/event#overview">
     *     Take a look at how many FMLStateEvents you can listen to via the @Mod.EventHandler annotation here
     * </a>
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("Hello From {}!", Tags.MOD_NAME);
        MbdCapabilities.registerCapability(ItemNBTMultiblockCapability.INSTANCE);

        NBTModifierFactory.register("minecraft:enchantment", NBTModifierEnchantment::deserialize);
        NBTRequirementFactory.register("minecraft:enchantment", NBTRequirementEnchantment::deserialize);
        NBTRequirementFactory.register("minecraft:item_id", NBTRequirementItem::deserialize);

        NBTRequirementFactory.register("minecraft:fluid_id", NBTRequirementFluid::deserialize);
        StorageProcessImplementation.register();
    }

}
