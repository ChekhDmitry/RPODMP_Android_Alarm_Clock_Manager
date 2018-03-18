package com.bsuir.chekh.lab2.service.alarm;


import com.bsuir.chekh.lab2.service.file.FileUtil;
import com.bsuir.chekh.lab2.model.AlarmModel;

import java.util.List;

public class AlarmService {

    private FileUtil fileUtil;
    public AlarmService() {
        fileUtil = new FileUtil();
    }

    public List<AlarmModel> getAll() {
        return fileUtil.readAlarmList();
    }

    public void writeAll(List<AlarmModel> alarms) {
        fileUtil.writeAlarmList(alarms);
    }
}
