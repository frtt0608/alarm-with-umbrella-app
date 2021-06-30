package com.heon9u.alarm_weather_app.alarm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.heon9u.alarm_weather_app.dto.Alarm;
import com.heon9u.alarm_weather_app.R;
import com.heon9u.alarm_weather_app.databinding.AlarmItemBinding;

import java.util.ArrayList;

public class AlarmAdapter extends ListAdapter<Alarm, AlarmAdapter.AlarmViewHolder> {

    Context context;
    ArrayList<Alarm> alarmList;
    public AlarmViewModel alarmViewModel;

    private LayoutInflater layoutInflater;

    public AlarmAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Alarm> DIFF_CALLBACK = new DiffUtil.ItemCallback<Alarm>() {
        @Override
        public boolean areItemsTheSame(@NonNull Alarm oldItem, @NonNull Alarm newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Alarm oldItem, @NonNull Alarm newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getHour() == newItem.getHour() &&
                    oldItem.getMinute() == newItem.getMinute() &&
                    oldItem.isTotalFlag() == newItem.isTotalFlag();
        }
    };

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        AlarmItemBinding itemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.alarm_item, parent, false);
        return new AlarmViewHolder(itemBinding);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public Alarm getAlarmAt(int position) {
        return getItem(position);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {

        Alarm alarm = alarmList.get(position);
        holder.alarmItemBinding.setAlarm(alarm);

        String hour = Integer.toString(alarm.getHour());
        String minute = Integer.toString(alarm.getMinute());
        hour = hour.length() < 2 ? "0"+hour : hour;
        minute = minute.length() < 2 ? "0"+minute : minute;
        holder.hour.setText(hour + "시");
        holder.minute.setText(minute + "분");

        if(alarm.isTotalFlag()) {
            holder.hour.setTextColor(Color.parseColor("#BB86FC"));
            holder.minute.setTextColor(Color.parseColor("#BB86FC"));
            holder.title.setTextColor(Color.parseColor("#BB86FC"));
            holder.totalSwitch.setChecked(true);

            if(alarm.isBasicSoundFlag() || alarm.isUmbSoundFlag()) {
                holder.sound.setBackgroundResource(R.drawable.sound_on);
            }
            if(alarm.isVibFlag()) {
                holder.vibrate.setBackgroundResource(R.drawable.vibrate_on);
            }
        } else {
            holder.hour.setTextColor(Color.parseColor("#D8D8D8"));
            holder.minute.setTextColor(Color.parseColor("#D8D8D8"));
            holder.title.setTextColor(Color.parseColor("#D8D8D8"));
            holder.totalSwitch.setChecked(false);

            holder.sound.setBackgroundResource(R.drawable.sound_off);
            holder.vibrate.setBackgroundResource(R.drawable.vibrate_off);
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
        AlarmSQLDatabase alarmDB = new AlarmSQLDatabase(context);
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

    public class AlarmViewHolder extends RecyclerView.ViewHolder {

        AlarmItemBinding alarmItemBinding;
        TextView hour, minute, title;
        Switch totalSwitch;
        ImageView sound, vibrate;

        public AlarmViewHolder(@NonNull AlarmItemBinding alarmItemBinding) {
            super(alarmItemBinding.getRoot());
            this.alarmItemBinding = alarmItemBinding;

            hour = itemView.findViewById(R.id.hour);
            minute = itemView.findViewById(R.id.minute);
            title = itemView.findViewById(R.id.title);
            totalSwitch = itemView.findViewById(R.id.totalSwitch);
            sound = itemView.findViewById(R.id.sound);
            vibrate = itemView.findViewById(R.id.vibrate);

            alarmItemBinding.cardView.setOnClickListener(v -> {
                int selectedItem = getBindingAdapterPosition();
                updateAlarm(v, selectedItem);
            });

            alarmItemBinding.cardView.setOnLongClickListener(v -> {
                int selectedItem = getBindingAdapterPosition();
                showDialogDeleteAlarm(selectedItem);

                return true;
            });

            totalSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int index = getBindingAdapterPosition();
                Alarm alarm = getItem(index);
                alarm.setTotalFlag(isChecked);

                AlarmSQLDatabase alarmDB = new AlarmSQLDatabase(context);
                alarmDB.changeTotalFlag(alarm.getId(), isChecked);
                alarmList.get(index).setTotalFlag(isChecked);

                if(isChecked) {
                    hour.setTextColor(Color.parseColor("#BB86FC"));
                    minute.setTextColor(Color.parseColor("#BB86FC"));
                    title.setTextColor(Color.parseColor("#BB86FC"));

                    if(alarm.isBasicSoundFlag() || alarm.isUmbSoundFlag()) {
                        sound.setBackgroundResource(R.drawable.sound_on);
                    }
                    if(alarm.isVibFlag()) {
                        vibrate.setBackgroundResource(R.drawable.vibrate_on);
                    }

                    changeAlarmOnOff(alarmList.get(index), "reboot");
                } else {
                    hour.setTextColor(Color.parseColor("#D8D8D8"));
                    minute.setTextColor(Color.parseColor("#D8D8D8"));
                    title.setTextColor(Color.parseColor("#D8D8D8"));
                    sound.setBackgroundResource(R.drawable.sound_off);
                    vibrate.setBackgroundResource(R.drawable.vibrate_off);
                    changeAlarmOnOff(alarmList.get(index), "cancel");
                }

                alarmDB.close();
            });
        }
    }

}