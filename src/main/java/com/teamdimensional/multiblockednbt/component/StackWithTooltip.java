package com.teamdimensional.multiblockednbt.component;

import com.cleanroommc.multiblocked.api.capability.IO;
import net.minecraft.item.ItemStack;

import java.util.LinkedList;
import java.util.List;

public class StackWithTooltip {
    public ItemStack stack;
    public final List<String> tooltip = new LinkedList<>();
    public final IO io;

    public StackWithTooltip(ItemStack stack, IO io) {
        this.stack = stack;
        this.io = io;
    }

    public void addTooltip(String line) {
        tooltip.add(line);
    }
}
