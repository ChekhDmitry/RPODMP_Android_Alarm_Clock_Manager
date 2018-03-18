package com.bsuir.chekh.lab2.controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bsuir.chekh.lab2.R;
import com.bsuir.chekh.lab2.model.AlarmModel;
import com.bsuir.chekh.lab2.service.alarm.AlarmService;
import com.bsuir.chekh.lab2.view.DividerItemDecoration;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.ArrayList;
import java.util.List;

public class AlarmListActivity extends AppCompatActivity {

    private static String SAVED_LIST_NAME = "saved_models";

    private AlarmListAdapter adapter;
    private List<AlarmModel> alarms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JodaTimeAndroid.init(this);

        fetchAlarmList(savedInstanceState);

        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_alarm_list);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        adapter = new AlarmListAdapter(this, alarms);
        recyclerView.setAdapter(adapter);

        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void fetchAlarmList(Bundle savedInstanceState) {
        try{
            alarms = savedInstanceState.getParcelableArrayList(SAVED_LIST_NAME);
        } catch (Exception ex) {
            alarms = new ArrayList<>(getAlarms());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if ((requestCode == 1) && (resultCode == Activity.RESULT_OK)) {
            try {
                AlarmModel result = data.getParcelableExtra(AlarmDetailActivity.RESULT_EXTRA_STRING);
                adapter.updateCurrentlySelected(result);
            } catch (Exception ex) {
                Log.d("onActivityResult", "Error in update action", ex);
            }
        }
    }

    private List<AlarmModel> getAlarms() {
        AlarmService  alarmService = new AlarmService();

        return alarmService.getAll();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                adapter.createNewAlarm();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVED_LIST_NAME, new ArrayList<Parcelable>(alarms));
    }
}
