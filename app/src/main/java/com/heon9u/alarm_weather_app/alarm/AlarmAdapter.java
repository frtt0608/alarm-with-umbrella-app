package com.heon9u.alarm_weather_app.alarm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.heon9u.alarm_weather_app.dto.Alarm;
import com.heon9u.alarm_weather_app.R;
import com.heon9u.alarm_weather_app.databinding.AlarmItemBinding;

import java.util.ArrayList;

public class AlarmAdapter extends ListAdapter<Alarm, AlarmAdapter.AlarmViewHolder> {

    private LayoutInflater layoutInflater;
    private OnItemClickListener listener;
    private OnCheckedChangeListener switchListener;

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
        Alarm alarm = getItem(position);
        holder.alarmItemBinding.setAlarm(alarm);
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

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });

            totalSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int position = getBindingAdapterPosition();
                if (switchListener != null && position != RecyclerView.NO_POSITION) {
                    switchListener.onItemClick(getItem(position), isChecked);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Alarm alarm);
    }

    public interface OnCheckedChangeListener {
        void onItemClick(Alarm alarm, boolean isChecked);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    public void setOnCheckedChangeListener(OnCheckedChangeListener switchListener) {
        this.switchListener = switchListener;
    }
}
