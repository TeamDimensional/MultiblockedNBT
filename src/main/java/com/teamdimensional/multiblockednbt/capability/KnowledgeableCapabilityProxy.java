package com.teamdimensional.multiblockednbt.capability;

import com.cleanroommc.multiblocked.api.capability.MultiblockCapability;
import com.cleanroommc.multiblocked.api.capability.proxy.CapCapabilityProxy;
import com.cleanroommc.multiblocked.api.definition.ControllerDefinition;
import com.cleanroommc.multiblocked.api.tile.ComponentTileEntity;
import com.cleanroommc.multiblocked.api.tile.ControllerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.capabilities.Capability;

public abstract class KnowledgeableCapabilityProxy<C, K> extends CapCapabilityProxy<C, K> {
    protected ControllerTileEntity controller;

    public KnowledgeableCapabilityProxy(MultiblockCapability<? super K> capability, TileEntity tileEntity, Capability<C> cap) {
        super(capability, tileEntity, cap);
    }

    public void setController(ComponentTileEntity<ControllerDefinition> controller) {
        assert controller instanceof ControllerTileEntity;
        this.controller = (ControllerTileEntity) controller;
    }
}
