package com.heon9u.alarm_weather_app.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.heon9u.alarm_weather_app.R;

import java.util.ArrayList;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder>{

    Context context;
    ArrayList alarm_id, alarm_time;
    private LayoutInflater layoutInflater;

    AppAdapter(Context context, ArrayList alarm_id, ArrayList alarm_time) {
        this.context = context;
        this.alarm_id = alarm_id;
        this.alarm_time = alarm_time;
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
        holder.alarm_id.setText(String.valueOf(alarm_id.get(position)));
        holder.alarm_time.setText(String.valueOf(alarm_time.get(position)));
    }

    @Override
    public int getItemCount() {
        return alarm_id.size();
    }

    public class AppViewHolder extends RecyclerView.ViewHolder {

        TextView alarm_id, alarm_time;

        public AppViewHolder(@NonNull View itemView) {
            super(itemView);
            alarm_id = itemView.findViewById(R.id.alarm_id);
            alarm_time = itemView.findViewById(R.id.alarm_time);
        }
    }
}
