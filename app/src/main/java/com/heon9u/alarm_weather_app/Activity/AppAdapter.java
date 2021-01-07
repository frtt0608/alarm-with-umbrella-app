package com.heon9u.alarm_weather_app.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
    Switch switch_button;

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
        holder.alarm_id.setText(String.valueOf(alarm.getId()));
        holder.hour.setText(alarm.getHour() + "시");
        holder.minute.setText(alarm.getMinute() + "분");

        UpdateListener updateListener = new UpdateListener();
        updateListener.applyData(alarm);

        DeleteListener deleteListener = new DeleteListener();
        deleteListener.applyData(alarm);

        SwitchChangeListener switchChangeListener = new SwitchChangeListener();
        switchChangeListener.applyData(holder, alarm.getId(), position);

        holder.cardView.setOnClickListener(updateListener);
        holder.delete_button.setOnClickListener(deleteListener);
        holder.switch_button.setOnCheckedChangeListener(switchChangeListener);

        if(alarm.getTotalFlag()) {
            holder.alarm_id.setTextColor(Color.parseColor("#000000"));
            holder.hour.setTextColor(Color.parseColor("#000000"));
            holder.minute.setTextColor(Color.parseColor("#000000"));
            holder.switch_button.setChecked(true);
        } else {
            holder.alarm_id.setTextColor(Color.parseColor("#D8D8D8"));
            holder.hour.setTextColor(Color.parseColor("#D8D8D8"));
            holder.minute.setTextColor(Color.parseColor("#D8D8D8"));
            holder.switch_button.setChecked(false);
        }
        System.out.println(alarm.getId() + ", " + alarm.getTotalFlag());
    }

    public class UpdateListener implements View.OnClickListener {

        Alarm alarm;

        void applyData(Alarm alarm) {
            this.alarm = alarm;
        }

        @Override
        public void onClick(View v) {
            Intent updateIntent = new Intent(v.getContext(), UpdateAlarmActivity.class);

            updateIntent.putExtra("Alarm", this.alarm);
            context.startActivity(updateIntent);

            ((Activity)context).finish();
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
                holder.alarm_id.setTextColor(Color.parseColor("#000000"));
                holder.hour.setTextColor(Color.parseColor("#000000"));
                holder.minute.setTextColor(Color.parseColor("#000000"));
                holder.switch_button.setChecked(true);
            } else {
                holder.alarm_id.setTextColor(Color.parseColor("#D8D8D8"));
                holder.hour.setTextColor(Color.parseColor("#D8D8D8"));
                holder.minute.setTextColor(Color.parseColor("#D8D8D8"));
                holder.switch_button.setChecked(false);
            }
//            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public class AppViewHolder extends RecyclerView.ViewHolder {

        TextView alarm_id, hour, minute;
        ImageButton delete_button;
        Switch switch_button;
        CardView cardView;

        public AppViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            alarm_id = itemView.findViewById(R.id.alarm_id);
            hour = itemView.findViewById(R.id.hour);
            minute = itemView.findViewById(R.id.minute);
            switch_button = itemView.findViewById(R.id.switch_button);
            delete_button = itemView.findViewById(R.id.delete_button);
        }
    }
}
