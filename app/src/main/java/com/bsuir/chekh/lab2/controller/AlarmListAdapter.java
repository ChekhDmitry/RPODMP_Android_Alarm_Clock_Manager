package com.bsuir.chekh.lab2.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bsuir.chekh.lab2.R;
import com.bsuir.chekh.lab2.model.AlarmModel;
import com.bsuir.chekh.lab2.service.alarm.AlarmManagerService;
import com.bsuir.chekh.lab2.service.alarm.AlarmService;

import java.util.List;

public class AlarmListAdapter
        extends RecyclerView.Adapter<AlarmListAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<AlarmModel> alarms;
    private AlarmService alarmService;
    private AlarmManagerService alarmManagerService;

    private int currentlySelected;

    AlarmListAdapter(Context context, List<AlarmModel> alarms) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.alarms = alarms;
        this.alarmService = new AlarmService();
        this.alarmManagerService = new AlarmManagerService();
    }

    @Override
    public AlarmListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.alarm_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlarmListAdapter.ViewHolder holder, int position) {
        AlarmModel alarm = alarms.get(position);

        holder.getDateView().setText(alarm.dateToFormattedString(false));
        holder.getSoundNameView().setText(alarm.getName());
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }



    public int getCurrentlySelected() {
        return currentlySelected;
    }

    public void setCurrentlySelected(int currentlySelected) {
        this.currentlySelected = currentlySelected;
    }


    public void updateCurrentlySelected(AlarmModel alarmModel) {
        if(alarmModel == null) return;

        if (currentlySelected == -1) {
            addNew(alarmModel);
        } else {
            changeExisting(alarmModel);
        }

        alarmService.writeAll(alarms);
    }

    private void changeExisting(AlarmModel alarmModel) {
        AlarmModel old = alarms.get(getCurrentlySelected());
        alarmManagerService.setAlarm(context, old, true);
        AlarmModel item = alarms.set(getCurrentlySelected(), alarmModel);
        this.notifyItemChanged(getCurrentlySelected());
        alarmManagerService.setAlarm(context, item, false);
    }

    private void addNew(AlarmModel alarmModel) {
        alarmManagerService.setAlarm(context, alarmModel, false);
        alarms.add(alarmModel);
        this.notifyDataSetChanged();
    }

    public void createNewAlarm() {
        setCurrentlySelected(-1);
        createNewActivity(null);
    }

    public void createNewActivity(AlarmModel item) {
        Intent intent = new Intent(context, AlarmDetailActivity.class);
        intent.putExtra(AlarmDetailActivity.ALARM_EXTRA_STRING, item);
        ((Activity)context).startActivityForResult(intent, 1);
    }

    public class ViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private final TextView dateView, soundNameView;

        ViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
            Button deleteBtn = (Button)(view.findViewById(R.id.btn_delete));
            deleteBtn.setOnClickListener(this);


            dateView = (TextView) view.findViewById(R.id.date);
            soundNameView = (TextView) view.findViewById(R.id.sound_name);
        }

        public TextView getSoundNameView() {
            return soundNameView;
        }

        public TextView getDateView() {
            return dateView;
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            if (v.getId() == R.id.btn_delete){
                deleteItem(position);
            } else {
                setCurrentlySelected(position);
                AlarmModel item = alarms.get(position);
                createNewActivity(item);
            }
        }
    }

    private void deleteItem(int position) {
        AlarmModel alarmModel = alarms.get(position);
        alarmManagerService.setAlarm(context, alarmModel, true);
        alarms.remove(position);
        this.notifyDataSetChanged();

        alarmService.writeAll(alarms);
    }
}
