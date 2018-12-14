package com.sky.chowder.model.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sky.chowder.model.ActivityModel;

import java.lang.reflect.Type;

/**
 * 序列化成json
 */
public class ModelSerial implements JsonSerializer<ActivityModel> {

    @Override
    public JsonElement serialize(ActivityModel model, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("class", model.getClassName());
        json.addProperty("component", model.getComponentName());
        json.addProperty("image", model.getImg());
        json.addProperty("des", model.getDescribe());
        return json;
    }
}