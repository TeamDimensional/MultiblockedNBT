package com.teamdimensional.multiblockednbt.factory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teamdimensional.multiblockednbt.api.INBTModifier;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class NBTModifierFactory {

    public interface NBTModifierSAM {
        @Nullable INBTModifier<?> create(JsonElement data);
    }

    private static final Map<String, NBTModifierSAM> modifiers = new HashMap<>();
    public static void register(String name, NBTModifierSAM method) {
        modifiers.put(name, method);
    }

    public static @Nullable INBTModifier<?> deserialize(JsonElement elt1) {
        if (!(elt1 instanceof JsonObject)) return null;
        JsonObject elt = (JsonObject) elt1;
        String type = elt.get("type").getAsString();
        if (!(modifiers.containsKey(type))) return null;
        JsonElement data = elt.get("data");
        return modifiers.get(type).create(data);
    }

    public static JsonObject serialize(INBTModifier<?> modifier) {
        JsonElement elt = modifier.serialize();
        JsonObject obj = new JsonObject();
        obj.addProperty("type", modifier.getName());
        obj.add("data", elt);
        return obj;
    }

}
