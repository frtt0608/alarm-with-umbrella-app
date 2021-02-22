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
        return new AlarmViewHolder(view);
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

        if(alarm.isTotalFlag()) {
            holder.hour.setTextColor(Color.parseColor("#BB86FC"));
            holder.minute.setTextColor(Color.parseColor("#BB86FC"));
            holder.title.setTextColor(Color.parseColor("#BB86FC"));
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

    public void updateAlarm(View v, int index) {
        Intent updateIntent = new Intent(v.getContext(), AlarmSetActivity.class);
        updateIntent.putExtra("alarm", alarmList.get(index));
        updateIntent.putExtra("REQUEST_STATE", "update");
        context.startActivity(updateIntent);
    }

    public void deleteAlarm(int index) {
        Alarm alarm = alarmList.get(index);
        AlarmDatabase alarmDB = new AlarmDatabase(context);
        int result = alarmDB.deleteAlarm(alarm.getId());

        if(result > 0) {
            alarmList.remove(alarm);
            changeAlarmOnOff(alarm, "cancel");
            notifyDataSetChanged();
        } else {
            Toast.makeText(context, "알람을 제거하지 못했습니다.", Toast.LENGTH_SHORT).show();
        }

        alarmDB.close();
    }

    public void showDialogDeleteAlarm(int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("알람 삭제")
                .setMessage("해당 알람을 삭제하시겠습니까?")
                .setIcon(android.R.drawable.ic_menu_delete)
                .setCancelable(false)
                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAlarm(index);
                    }
                });
        builder.setNegativeButton("취소", null);
        builder.show();
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public class AlarmViewHolder extends RecyclerView.ViewHolder {

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

            cardView.setOnClickListener(v -> {
                int selectedItem = getBindingAdapterPosition();
                updateAlarm(v, selectedItem);
            });

            cardView.setOnLongClickListener(v -> {
                int selectedItem = getBindingAdapterPosition();
                showDialogDeleteAlarm(selectedItem);

                return true;
            });

            totalSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int index = getBindingAdapterPosition();
                Alarm alarm = alarmList.get(index);
                AlarmDatabase alarmDB = new AlarmDatabase(context);
                alarmDB.changeTotalFlag(alarm.getId(), isChecked);
                alarmList.get(index).setTotalFlag(isChecked);

                if(isChecked) {
                    hour.setTextColor(Color.parseColor("#BB86FC"));
                    minute.setTextColor(Color.parseColor("#BB86FC"));
                    title.setTextColor(Color.parseColor("#BB86FC"));
                    changeAlarmOnOff(alarmList.get(index), "reboot");
                } else {
                    hour.setTextColor(Color.parseColor("#D8D8D8"));
                    minute.setTextColor(Color.parseColor("#D8D8D8"));
                    title.setTextColor(Color.parseColor("#D8D8D8"));
                    changeAlarmOnOff(alarmList.get(index), "cancel");
                }
                alarmDB.close();
            });
        }
    }
}
