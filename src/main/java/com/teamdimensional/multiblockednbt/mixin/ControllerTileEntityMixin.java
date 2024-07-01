package com.teamdimensional.multiblockednbt.mixin;

import com.cleanroommc.multiblocked.api.capability.IO;
import com.cleanroommc.multiblocked.api.capability.MultiblockCapability;
import com.cleanroommc.multiblocked.api.capability.proxy.CapabilityProxy;
import com.cleanroommc.multiblocked.api.definition.ControllerDefinition;
import com.cleanroommc.multiblocked.api.tile.ComponentTileEntity;
import com.cleanroommc.multiblocked.api.tile.ControllerTileEntity;
import com.google.common.collect.Table;
import com.teamdimensional.multiblockednbt.capability.KnowledgeableCapabilityProxy;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ControllerTileEntity.class, remap = false)
abstract public class ControllerTileEntityMixin extends ComponentTileEntity<ControllerDefinition> {
    @Shadow
    protected Table<IO, MultiblockCapability<?>, Long2ObjectOpenHashMap<CapabilityProxy<?>>> capabilities;

    @Inject(method = "onStructureFormed", at = @At("TAIL"))
    void doOnStructureFormed(CallbackInfo ci) {
        for (Long2ObjectOpenHashMap<CapabilityProxy<?>> map : capabilities.values()) {
            for (CapabilityProxy<?> proxy : map.values()) {
                if (proxy instanceof KnowledgeableCapabilityProxy<?, ?>) {
                    ((KnowledgeableCapabilityProxy<?, ?>) proxy).setController(this);
                }
            }
        }
    }
}
