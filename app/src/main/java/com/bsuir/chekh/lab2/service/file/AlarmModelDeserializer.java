package com.bsuir.chekh.lab2.service.file;

import com.bsuir.chekh.lab2.model.AlarmModel;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.joda.time.DateTime;

import java.lang.reflect.Type;


public class AlarmModelDeserializer implements JsonDeserializer<AlarmModel> {


    @Override
    public AlarmModel deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jobject =jsonElement.getAsJsonObject();

        AlarmModel alarmModel = new AlarmModel(jobject.get("sound").getAsString(),
                new DateTime(jobject.get("date").getAsLong()));
        alarmModel.setName(jobject.get("name").getAsString());
        return alarmModel;
    }
}
