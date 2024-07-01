package com.teamdimensional.multiblockednbt.mixin;

import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.ArrayList;
import java.util.List;

public class LateMixin implements ILateMixinLoader {
    @Override
    public List<String> getMixinConfigs() {
        List<String> lst = new ArrayList<>();
        lst.add("mixins.multiblockednbt.json");
        return lst;
    }
}
