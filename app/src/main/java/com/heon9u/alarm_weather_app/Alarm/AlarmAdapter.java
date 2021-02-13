package com.heon9u.alarm_weather_app.Alarm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.heon9u.alarm_weather_app.Dto.Alarm;
import com.heon9u.alarm_weather_app.R;

import java.util.ArrayList;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    Context context;
    ArrayList<Alarm> alarmList;

    private LayoutInflater layoutInflater;

    AlarmAdapter(Context context, ArrayList alarmList) {
        this.context = context;
        this.alarmList = alarmList;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.alarm_item, parent, false);

        return new AlarmAdapter.AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {

        Alarm alarm = alarmList.get(position);
        String hour = Integer.toString(alarm.getHour());
        String minute = Integer.toString(alarm.getMinute());
        hour = hour.length() < 2 ? "0"+hour : hour;
        minute = minute.length() < 2 ? "0"+minute : minute;
        holder.hour.setText(hour + "시");
        holder.minute.setText(minute + "분");
        holder.title.setText(alarm.getTitle());

        UpdateListener updateListener = new UpdateListener();
        updateListener.applyData(alarm);
        DeleteListener deleteListener = new DeleteListener();
        deleteListener.applyData(alarm);
        SwitchChangeListener switchChangeListener = new SwitchChangeListener();
        switchChangeListener.applyData(holder, alarm.getId(), position);

        holder.cardView.setOnClickListener(updateListener);
        holder.cardView.setOnLongClickListener(deleteListener);
        holder.totalSwitch.setOnCheckedChangeListener(switchChangeListener);

        if(alarm.isTotalFlag()) {
            holder.hour.setTextColor(Color.parseColor("#FFBB86FC"));
            holder.minute.setTextColor(Color.parseColor("#FFBB86FC"));
            holder.title.setTextColor(Color.parseColor("#FFBB86FC"));
            holder.totalSwitch.setChecked(true);
        } else {
            holder.hour.setTextColor(Color.parseColor("#D8D8D8"));
            holder.minute.setTextColor(Color.parseColor("#D8D8D8"));
            holder.title.setTextColor(Color.parseColor("#D8D8D8"));
            holder.totalSwitch.setChecked(false);
        }
    }

    public void changeAlarmOnOff(Alarm alarm, String request) {
        Intent alarmIntent = new Intent(context, AlarmManagerActivity.class);
        alarmIntent.putExtra("alarm", alarm);
        alarmIntent.putExtra("request", request);
        context.startActivity(alarmIntent);
    }

    public class UpdateListener implements View.OnClickListener {

        Alarm alarm;

        void applyData(Alarm alarm) {
            this.alarm = alarm;
        }

        @Override
        public void onClick(View v) {
            Intent updateIntent = new Intent(v.getContext(), AlarmSetActivity.class);
            updateIntent.putExtra("alarm", this.alarm);
            updateIntent.putExtra("REQUEST_STATE", "update");
            context.startActivity(updateIntent);
        }
    }

    public class DeleteListener implements View.OnLongClickListener {

        Alarm alarm;

        void applyData(Alarm alarm) {
            this.alarm = alarm;
        }

        @Override
        public boolean onLongClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete");
            builder.setMessage("Are you sure to delete ??");
            builder.setIcon(android.R.drawable.ic_menu_delete);
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AlarmDatabase alarmDB = new AlarmDatabase(context);
                    int result = alarmDB.deleteAlarm(alarm.getId());

                    if(result > 0) {
                        alarmList.remove(alarm);
                        changeAlarmOnOff(alarm, "cancel");
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                    }

                    alarmDB.close();
                }
            });

            builder.setNegativeButton("No", null);
            builder.show();

            return true;
        }
    }

    public class SwitchChangeListener implements CompoundButton.OnCheckedChangeListener {

        int id, position;
        AlarmViewHolder holder;

        void applyData(AlarmViewHolder holder, int id, int position) {
            this.holder = holder;
            this.id = id;
            this.position = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            AlarmDatabase alarmDB = new AlarmDatabase(context);
            alarmDB.changeTotalFlag(this.id, isChecked);
            alarmList.get(position).setTotalFlag(isChecked);

            if(isChecked) {
                holder.hour.setTextColor(Color.parseColor("#FFBB86FC"));
                holder.minute.setTextColor(Color.parseColor("#FFBB86FC"));
                holder.title.setTextColor(Color.parseColor("#FFBB86FC"));
                changeAlarmOnOff(alarmList.get(position), "reboot");
            } else {
                holder.hour.setTextColor(Color.parseColor("#D8D8D8"));
                holder.minute.setTextColor(Color.parseColor("#D8D8D8"));
                holder.title.setTextColor(Color.parseColor("#D8D8D8"));
                changeAlarmOnOff(alarmList.get(position), "cancel");
            }
            alarmDB.close();
        }
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public static class AlarmViewHolder extends RecyclerView.ViewHolder {

        TextView hour, minute, title;
        Switch totalSwitch;
        CardView cardView;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            hour = itemView.findViewById(R.id.hour);
            minute = itemView.findViewById(R.id.minute);
            title = itemView.findViewById(R.id.title);
            totalSwitch = itemView.findViewById(R.id.totalSwitch);
        }
    }
}
