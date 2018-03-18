package com.bsuir.chekh.lab2.service.alarm;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.bsuir.chekh.lab2.model.AlarmModel;

public class AlarmManagerService {

    public void setAlarm(Context context, AlarmModel model, boolean isCancel)
    {
        if(model == null) {
            return;
        }

        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(AlarmReceiver.ALARM_EXTRA_STRING, model);

        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        if(!isCancel) {
            setAlarm(model, am, pi);
        } else {
            cancelAlarm(am, pi);
        }
    }

    private void setAlarm(AlarmModel model, AlarmManager am, PendingIntent pi) {
        am.set(AlarmManager.RTC_WAKEUP, model.getDate().getMillis(), pi);
    }

    private void cancelAlarm(AlarmManager alarmManager, PendingIntent sender) {
        alarmManager.cancel(sender);
    }
}
