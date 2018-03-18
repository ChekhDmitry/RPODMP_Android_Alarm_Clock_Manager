package com.bsuir.chekh.lab2.service.alarm;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.util.Log;
import android.view.WindowManager;

import com.bsuir.chekh.lab2.model.AlarmModel;

import net.danlew.android.joda.JodaTimeAndroid;

public class AlarmReceiver extends BroadcastReceiver {

    public static String ALARM_EXTRA_STRING = "alarm_handler_string";

    @Override
    public void onReceive(Context context, Intent intent) {
        JodaTimeAndroid.init(context);

        tryToPlayAlarm(context, intent);

    }

    private void tryToPlayAlarm(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        play(context, intent);
        wl.release();
    }

    private void play(Context context, Intent intent) {
        AlarmModel result= intent.getParcelableExtra(AlarmReceiver.ALARM_EXTRA_STRING);
        if(result != null) {
            Ringtone r = RingtoneManager.getRingtone(context, Uri.parse(result.getUri()));
            if (r != null) {
                r.play();
                try {
                    displayAlert(context, r, result);
                } catch (Exception ex) {
                    Log.d("Alarm", "Can't display alert", ex);
                }
            }
        }
    }

    private void displayAlert(Context context, final Ringtone r, AlarmModel model)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setCancelable(false)
                .setTitle("ALARM")
                .setMessage(model.getName())
                .setNegativeButton("Disable",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        r.stop();
                    }
                });

        AlertDialog alert = builder.create();
        if(alert.getWindow() != null) {
            alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            alert.show();
        }
    }
}