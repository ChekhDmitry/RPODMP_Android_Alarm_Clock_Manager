package com.bsuir.chekh.lab2.service.file;


import android.os.Environment;
import android.util.Log;

import com.bsuir.chekh.lab2.model.AlarmModel;
import com.google.gson.GsonBuilder;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.io.FileUtils.readFileToString;

public class FileUtil {

    private final String fileName = Environment.getExternalStorageDirectory() + "/alarmFile.txt";

    public  List<AlarmModel> readAlarmList() {
        String content;
        try {
            content = readFile(fileName, "UTF-8");
        } catch (Exception ex) {
            return new ArrayList<>();
        }

        GsonBuilder gsonBuilder = getGsonBuilder();
        AlarmModel[]  models = gsonBuilder.create().fromJson(content, AlarmModel[].class);

        return new ArrayList<>(Arrays.asList(models));
    }

    public void writeAlarmList(List<AlarmModel> alarmModels) {
        GsonBuilder gsonBuilder = getGsonBuilder();
        String alarms = gsonBuilder.create().toJson(alarmModels.toArray());


        try {
            save(fileName, "UTF-8", alarms);
        } catch (Exception ex) {
            Log.d(ex.toString(), "err");
        }
    }

    private GsonBuilder getGsonBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(AlarmModel.class, new AlarmModelSerializer());
        gsonBuilder.registerTypeAdapter(AlarmModel.class, new AlarmModelDeserializer());
        return gsonBuilder;
    }


    private static String readFile(String path, String encoding)
            throws IOException
    {
        File file = new File(path);
        return readFileToString(file, encoding);
    }

    private static void save(String path, String encoding, String data)
            throws IOException {
        File file = new File(path);
        FileUtils.writeStringToFile(file, data, encoding);
    }
}

