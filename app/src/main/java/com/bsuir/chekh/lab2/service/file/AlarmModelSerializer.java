package com.bsuir.chekh.lab2.service.file;

import com.bsuir.chekh.lab2.model.AlarmModel;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class AlarmModelSerializer implements JsonSerializer<AlarmModel> {

    @Override
    public JsonElement serialize(AlarmModel src, Type typeOfSrc,
                                 JsonSerializationContext context)
    {

        JsonObject obj = new JsonObject();
        obj.addProperty("sound", src.getUri());
        obj.addProperty("name", src.getName());
        obj.addProperty("date", Long.toString(src.getDate().getMillis()));

        return obj;
    }

}

