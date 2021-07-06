package com.heon9u.alarm_weather_app.alarm;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.heon9u.alarm_weather_app.dto.Alarm;
import com.heon9u.alarm_weather_app.R;
import com.heon9u.alarm_weather_app.databinding.AlarmItemBinding;


public class AlarmAdapter extends ListAdapter<Alarm, AlarmAdapter.AlarmViewHolder> {

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
                    oldItem.isTotalFlag() == newItem.isTotalFlag() &&
                    oldItem.getDay().equals(newItem.getDay()) &&
                    oldItem.getBasicSoundUri().equals(newItem.getBasicSoundUri()) &&
                    oldItem.getUmbSoundUri().equals(newItem.getUmbSoundUri());
        }
    };

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        AlarmItemBinding itemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.alarm_item, parent, false);
        return new AlarmViewHolder(itemBinding);
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

        public AlarmViewHolder(@NonNull AlarmItemBinding alarmItemBinding) {
            super(alarmItemBinding.getRoot());
            this.alarmItemBinding = alarmItemBinding;

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });

            alarmItemBinding.totalSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int position = getBindingAdapterPosition();
                if (switchListener != null && position != RecyclerView.NO_POSITION) {
                    Alarm alarm = getAlarmAt(position);
                    alarm.setTotalFlag(isChecked);
                    switchListener.onItemClick(alarm);

                    if(isChecked) {
                        alarmItemBinding.hour.setTextColor(Color.parseColor("#BB86FC"));
                        alarmItemBinding.minute.setTextColor(Color.parseColor("#BB86FC"));
                        alarmItemBinding.title.setTextColor(Color.parseColor("#BB86FC"));

                    } else {
                        alarmItemBinding.hour.setTextColor(Color.parseColor("#D8D8D8"));
                        alarmItemBinding.minute.setTextColor(Color.parseColor("#D8D8D8"));
                        alarmItemBinding.title.setTextColor(Color.parseColor("#D8D8D8"));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Alarm alarm);
    }

    public interface OnCheckedChangeListener {
        void onItemClick(Alarm alarm);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    public void setOnCheckedChangeListener(OnCheckedChangeListener switchListener) {
        this.switchListener = switchListener;
    }
}
