package com.teamdimensional.multiblockednbt.factory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teamdimensional.multiblockednbt.api.INBTRequirement;
import com.teamdimensional.multiblockednbt.component.NBTModificationRecipe;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class NBTRequirementFactory {

    public interface NBTRequirementSAM {
        INBTRequirement<?> create(JsonElement data);
    }

    private static final Map<String, NBTRequirementSAM> requirements = new HashMap<>();
    public static void register(String name, NBTRequirementSAM method) {
        requirements.put(name, method);
    }

    public static @Nullable INBTRequirement<?> deserialize(JsonElement elt1) {
        if (!(elt1 instanceof JsonObject)) return null;
        JsonObject elt = (JsonObject) elt1;
        String type = elt.get("type").getAsString();
        if (!(requirements.containsKey(type))) return null;
        JsonElement data = elt.get("data");
        return requirements.get(type).create(data);
    }

    public static JsonObject serialize(INBTRequirement<?> requirement) {
        JsonElement elt = requirement.serialize();
        JsonObject obj = new JsonObject();
        obj.addProperty("type", requirement.getName());
        obj.add("data", elt);
        return obj;
    }

}
