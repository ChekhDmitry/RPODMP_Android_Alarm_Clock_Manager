package com.bsuir.chekh.lab2.controller;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bsuir.chekh.lab2.R;
import com.bsuir.chekh.lab2.model.AlarmModel;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class AlarmDetailActivity
        extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private static String SAVED_MODEL = "saved_model";
    public static String ALARM_EXTRA_STRING = "alarm";
    public static String RESULT_EXTRA_STRING = "result";

    private TextView textDateView;
    private AlarmModel alarm;
    private Spinner spinner;
    private List<AlarmModel> soundList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        soundList = getNotificationSounds();

        fetchModel(savedInstanceState);

        textDateView.setText(alarm.dateToFormattedString(true));

        textDateView.setOnClickListener(createOnClickListener());
    }

    private View.OnClickListener createOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTime date = alarm.getDate();

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AlarmDetailActivity.this,
                        AlarmDetailActivity.this,
                        date.getYear(),
                        date.getMonthOfYear()-1,
                        date.getDayOfMonth());

                datePickerDialog.show();
            }
        };
    }

    private void fetchModel(Bundle savedInstanceState) {
        Intent i = getIntent();
        try {
            alarm = savedInstanceState.getParcelable(SAVED_MODEL);
        } catch (Exception ex) {
            alarm = i.getParcelableExtra(ALARM_EXTRA_STRING);
        }

        if(alarm == null) {
            alarm = new AlarmModel("dummy", new DateTime());
        }
    }

    private void initView() {
        setContentView(R.layout.activity_detail);

        spinner = (Spinner) findViewById(R.id.sounds);
        ArrayAdapter<String> adapter
                = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getSoundsNames());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        textDateView = (TextView) findViewById(R.id.date_text_view);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (view.isShown()) {
            DateTime date = alarm.getDate();
            date = date.withYear(year)
                    .withMonthOfYear(month + 1)
                    .withDayOfMonth(dayOfMonth);
            alarm.setDate(date);

            textDateView.setText(alarm.dateToFormattedString(true));

            TimePickerDialog timePickerDialog
                    = new TimePickerDialog(this, AlarmDetailActivity.this, date.getHourOfDay(), date.getMinuteOfHour(), true);
            timePickerDialog.show();
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (view.isShown()) {
            DateTime date = alarm.getDate();
            date = date
                    .withHourOfDay(hourOfDay)
                    .withMinuteOfHour(minute);
            alarm.setDate(date);

            textDateView.setText(alarm.dateToFormattedString(true));
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    public ArrayList<String> getSoundsNames() {
        List<AlarmModel> alarms = getNotificationSounds();
        ArrayList<String> result = new ArrayList<>();
        for (AlarmModel alarmModel : alarms) {
            result.add(alarmModel.getName());
        }
        return result;
    }

    public ArrayList<AlarmModel> getNotificationSounds() {
        RingtoneManager manager = new RingtoneManager(this);
        manager.setType(RingtoneManager.TYPE_ALARM);
        Cursor cursor;
        try {
            cursor = manager.getCursor();

        } catch (Exception ex) {
            Log.d("Error","Permissions denied");
            return new ArrayList<>();
        }

        ArrayList<AlarmModel> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(RingtoneManager.ID_COLUMN_INDEX);
            String uri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX);

            AlarmModel alarmModel = new AlarmModel(uri + "/" + id, null);
            alarmModel.setName(cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX));
            list.add(alarmModel);
        }

        return list;
    }

    public void onSaveClick(View view) {
        int resultCode = 1;
        AlarmModel selectedSound = soundList.get((int)spinner.getSelectedItemId());
        alarm.setUri(selectedSound.getUri());
        alarm.setName(selectedSound.getName());
        alarm.setDate(alarm.getDate().withSecondOfMinute(0));

        if(alarm.getDate().compareTo(new DateTime()) <= 0) {
            Toast.makeText(this, "Invalid date", Toast.LENGTH_SHORT).show();
            return;
        }


        Intent resultIntent = new Intent();
        resultIntent.putExtra(RESULT_EXTRA_STRING, alarm);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_MODEL, alarm);
    }
}
