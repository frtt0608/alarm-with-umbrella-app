package com.heon9u.alarm_weather_app.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.heon9u.alarm_weather_app.Dto.Alarm;
import com.heon9u.alarm_weather_app.R;

import java.util.ArrayList;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder>{

    Context context;
    ArrayList<Alarm> alarmList;
    AlarmActivity alarmActivity;

    private LayoutInflater layoutInflater;

    AppAdapter(Context context, ArrayList alarmList) {
        this.context = context;
        this.alarmList = alarmList;
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.alarm_item, parent, false);

        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {

        Alarm alarm = alarmList.get(position);
        holder.hour.setText(alarm.getHour() + "시");
        holder.minute.setText(alarm.getMinute() + "분");
        holder.title.setText(alarm.getTitle());

        UpdateListener updateListener = new UpdateListener();
        updateListener.applyData(alarm);

        DeleteListener deleteListener = new DeleteListener();
        deleteListener.applyData(alarm);

        SwitchChangeListener switchChangeListener = new SwitchChangeListener();
        switchChangeListener.applyData(holder, alarm.getId(), position);

        holder.cardView.setOnClickListener(updateListener);
        holder.delete_button.setOnClickListener(deleteListener);
        holder.totalSwitch.setOnCheckedChangeListener(switchChangeListener);

        if(alarm.isTotalFlag()) {
            holder.hour.setTextColor(Color.parseColor("#000000"));
            holder.minute.setTextColor(Color.parseColor("#000000"));
            holder.title.setTextColor(Color.parseColor("#000000"));
            holder.totalSwitch.setChecked(true);
        } else {
            holder.hour.setTextColor(Color.parseColor("#D8D8D8"));
            holder.minute.setTextColor(Color.parseColor("#D8D8D8"));
            holder.title.setTextColor(Color.parseColor("#D8D8D8"));
            holder.totalSwitch.setChecked(false);
        }
    }

    public void changeAlarmOnOff(Alarm alarm, String request) {
        Intent alarmIntent = new Intent(context, AlarmActivity.class);
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
            Intent updateIntent = new Intent(v.getContext(), SetAlarmActivity.class);
            updateIntent.putExtra("alarm", this.alarm);
            context.startActivity(updateIntent);
        }
    }

    public class DeleteListener implements View.OnClickListener {

        Alarm alarm;

        void applyData(Alarm alarm) {
            this.alarm = alarm;
        }

        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete");
            builder.setMessage("Are you sure to delete " + alarm.getId() + " ??");
            builder.setIcon(android.R.drawable.ic_menu_delete);

            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AppDatabaseHelper appDB = new AppDatabaseHelper(context);
                    int result = appDB.deleteAlarm(alarm.getId());

                    if(result > 0) {
                        Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
                        alarmList.remove(alarm);
                        changeAlarmOnOff(alarm, "cancel");
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            builder.setNegativeButton("No", null);
            builder.show();
        }
    }

    public class SwitchChangeListener implements CompoundButton.OnCheckedChangeListener {

        int id, position;
        AppViewHolder holder;

        void applyData(AppViewHolder holder, int id, int position) {
            this.holder = holder;
            this.id = id;
            this.position = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            AppDatabaseHelper appDB = new AppDatabaseHelper(context);
            appDB.changeTotalFlag(this.id, isChecked);
            alarmList.get(position).setTotalFlag(isChecked);

            if(isChecked) {
                holder.hour.setTextColor(Color.parseColor("#000000"));
                holder.minute.setTextColor(Color.parseColor("#000000"));
                holder.title.setTextColor(Color.parseColor("#000000"));
                changeAlarmOnOff(alarmList.get(position), "create");
            } else {
                holder.hour.setTextColor(Color.parseColor("#D8D8D8"));
                holder.minute.setTextColor(Color.parseColor("#D8D8D8"));
                holder.title.setTextColor(Color.parseColor("#D8D8D8"));
                changeAlarmOnOff(alarmList.get(position), "cancel");
            }
//            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public class AppViewHolder extends RecyclerView.ViewHolder {

        TextView hour, minute, title;
        ImageButton delete_button;
        Switch totalSwitch;
        CardView cardView;

        public AppViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            hour = itemView.findViewById(R.id.hour);
            minute = itemView.findViewById(R.id.minute);
            title = itemView.findViewById(R.id.title);
            totalSwitch = itemView.findViewById(R.id.totalSwitch);
            delete_button = itemView.findViewById(R.id.delete_button);
        }
    }
}
