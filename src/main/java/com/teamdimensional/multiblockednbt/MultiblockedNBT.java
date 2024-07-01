package com.teamdimensional.multiblockednbt;

import com.cleanroommc.multiblocked.api.registry.MbdCapabilities;
import com.teamdimensional.multiblockednbt.capability.ItemNBTMultiblockCapability;
import com.teamdimensional.multiblockednbt.capability.StorageProcessImplementation;
import com.teamdimensional.multiblockednbt.factory.NBTModifierFactory;
import com.teamdimensional.multiblockednbt.factory.NBTRequirementFactory;
import com.teamdimensional.multiblockednbt.modifier.NBTModifierEnchantment;
import com.teamdimensional.multiblockednbt.requirement.NBTRequirementEnchantment;
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
        NBTModifierFactory.register("ench", NBTModifierEnchantment::deserialize);
        NBTRequirementFactory.register("ench", NBTRequirementEnchantment::deserialize);
        StorageProcessImplementation.register();
    }

}
